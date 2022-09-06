package com.zhevakin.requester.front.generator;

import com.zhevakin.requester.front.controller.MainController;
import com.zhevakin.requester.model.RequestInfo;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TreeGenerator {

    private final ContextMenuGenerator contextMenuGenerator;

    @Autowired
    public TreeGenerator(ContextMenuGenerator contextMenuGenerator) {
        this.contextMenuGenerator = contextMenuGenerator;
    }

    public static void createTree(MainController mainController) {
        TreeView<RequestInfo> treeView = mainController.getRequestTree();
        TreeItem<RequestInfo> treeItem = new TreeItem<>(null);
        treeView.setRoot(treeItem);
        treeView.setShowRoot(false);
    }

    private void handleMouseClicked(MouseEvent event, TreeView<RequestInfo> treeView) {
        // TODO: перенаследоваться от CellFactory и сделать нормальный обработчик событий
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null) && event.isSecondaryButtonDown()) {
            RequestInfo requestInfo = treeView.getSelectionModel().getSelectedItem().getValue();
            treeView.setContextMenu(contextMenuGenerator.generateMenu(requestInfo, treeView));
            System.out.println(requestInfo.toString());
        }
    }

    public void fillTree(TreeView<RequestInfo> treeView, List<RequestInfo> requestsInfo) {

        List<RequestInfo> collections = new ArrayList<>();
        List<RequestInfo> folders = new ArrayList<>();
        List<RequestInfo> requests = new ArrayList<>();
        List<RequestInfo> tempList = new ArrayList<>();

        for (RequestInfo requestInfo : requestsInfo) {
            switch (requestInfo.getTypeRequest()) {
                case COLLECTIONS: collections.add(requestInfo); break;
                case FOLDER: folders.add(requestInfo); break;
                case REQUEST: requests.add(requestInfo); break;
            }
        }

        TreeItem<RequestInfo> root = treeView.getRoot();
        root.getChildren().clear();

        for (RequestInfo collection : collections) {
            TreeItem<RequestInfo> item = new TreeItem<>(collection);
            IconGenerator.setIcon(item);
            root.getChildren().add(item);
        }

        for (RequestInfo folder : folders) {
            if (!treeParser(root, folder))
                tempList.add(folder);
        }

        for (RequestInfo request : requests) {
            if (!treeParser(root, request))
                tempList.add(request);
        }

        for (RequestInfo temp : tempList)
            treeParser(root, temp);

        EventHandler<MouseEvent> mouseEventHandler = (MouseEvent event) -> {
            handleMouseClicked(event, treeView);
        };

//        treeView.addEventHandler(DragEvent.);
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandler);

    }

    public boolean treeParser(TreeItem<RequestInfo> root, RequestInfo requestInfo) {

        if (requestInfo.getParent() == null || requestInfo.getParent().equals("")) return false;
        ObservableList<TreeItem<RequestInfo>> children = root.getChildren();
        if (root.getValue() != null) {
            if (root.getValue().getId().equals(requestInfo.getParent())) {
                TreeItem<RequestInfo> item = new TreeItem<>(requestInfo);
                IconGenerator.setIcon(item);
                root.getChildren().add(item);
                return true;
            }
        }
        for (TreeItem<RequestInfo> treeItem : children) {

            RequestInfo child = treeItem.getValue();
            if (requestInfo.getParent().equals(child.getId())) {
                TreeItem<RequestInfo> item = new TreeItem<>(requestInfo);
                IconGenerator.setIcon(item);
                treeItem.getChildren().add(item);
                return true;
            }
            if (treeParser(treeItem, requestInfo))
                return true;
        }
        return false;

    }

}
