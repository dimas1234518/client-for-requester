package com.zhevakin.requester.front.generator;

import com.zhevakin.requester.additional.Import;
import com.zhevakin.requester.additional.impl.ImportImpl;
import com.zhevakin.requester.facade.SyncFacade;
import com.zhevakin.requester.model.RequestInfo;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ContextMenuGenerator {

    private final SyncFacade syncFacade;

    @Autowired
    public ContextMenuGenerator(SyncFacade syncFacade) {
        this.syncFacade = syncFacade;
    }

    public ContextMenu generateMenu(RequestInfo requestInfo, TreeView<RequestInfo> treeView) {

        ContextMenu contextMenu = new ContextMenu();
        List<MenuItem> itemsList = new ArrayList<>();

        switch (requestInfo.getTypeRequest()) {
            case COLLECTIONS: {

                createAddItems(itemsList, treeView);

                MenuItem exportItem = new MenuItem();
                exportItem.setText("Export");
                exportItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        export(treeView);
                    }
                });

                MenuItem syncItem = new MenuItem();
                syncItem.setText("Synchronize");
                syncItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        syncFacade.syncCollection(requestInfo.getId());
                        //TODO: Событие на изменение данных в MainController (обновление дерева)
                    }
                });

                MenuItem saveItem = new MenuItem();
                saveItem.setText("Save on server");
                saveItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        syncFacade.saveCollection(saveOnServer(treeView));
                    }
                });

                itemsList.add(exportItem);
                itemsList.add(syncItem);
                itemsList.add(saveItem);

                break;
            }
            case FOLDER: {

                createAddItems(itemsList, treeView);

                break;
            }
            case REQUEST: {
                break;
            }
        }

        MenuItem delItem = new MenuItem();
        delItem.setText("Delete");
        delItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                delete(treeView);
            }
        });

        itemsList.add(delItem);

        contextMenu.getItems().addAll(itemsList);
        return contextMenu;

    }

    private List<RequestInfo> saveOnServer(TreeView<RequestInfo> treeView) {

        TreeItem<RequestInfo> currentNode = treeView.getSelectionModel().getSelectedItem();
        List<RequestInfo> requests = new ArrayList<>();
        ObservableList<TreeItem<RequestInfo>> children = currentNode.getChildren();
        requests.add(currentNode.getValue());

        for (TreeItem<RequestInfo> child : children) {
            requests.add(child.getValue());
            if (!child.isLeaf()) getLeafNodes(child, requests);
        }
        return requests;

    }

    private void createAddItems(List<MenuItem> itemList, TreeView<RequestInfo> treeView) {
        MenuItem addFolder = new MenuItem();
        addFolder.setText("Add folder");
        addFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addFolder(treeView);
            }
        });

        itemList.add(addFolder);

        MenuItem addRequest = new MenuItem();
        addRequest.setText("Add request");
        addRequest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addRequest(treeView);
            }
        });

        itemList.add(addRequest);

    }

    private void export(TreeView<RequestInfo> treeView) {

        TreeItem<RequestInfo> currentNode = treeView.getSelectionModel().getSelectedItem();
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Export Collections");
        File selectedDirectory = directoryChooser.showDialog(treeView.getScene().getWindow());
        if (selectedDirectory != null) {
            List<RequestInfo> requests = new ArrayList<>();
            ObservableList<TreeItem<RequestInfo>> children = currentNode.getChildren();
            requests.add(currentNode.getValue());

            for (TreeItem<RequestInfo> child : children) {
                requests.add(child.getValue());
                if (!child.isLeaf()) getLeafNodes(child, requests);
            }
            Import importer = new ImportImpl();
            importer.saveRequests(requests, selectedDirectory.getPath() + "/" + currentNode.getValue().getName() + ".json");
        }
    }

    private void getLeafNodes(TreeItem<RequestInfo> currentNode, List<RequestInfo> requests) {
        ObservableList<TreeItem<RequestInfo>> children = currentNode.getChildren();
        for (TreeItem<RequestInfo> child : children) {
            requests.add(child.getValue());
            if (!child.isLeaf()) getLeafNodes(child, requests);
        }
    }

    private void delete(TreeView<RequestInfo> treeView) {

        TreeItem<RequestInfo> currentNode = treeView.getSelectionModel().getSelectedItem();
        currentNode.getParent().getChildren().remove(currentNode);

    }

    private void addRequest(TreeView<RequestInfo> treeView) {
        TreeItem<RequestInfo> currentNode = treeView.getSelectionModel().getSelectedItem();
        RequestInfo requestInfo = RequestGenerator.createRequest();
        requestInfo.setParent(currentNode.getValue().getId());
        TreeItem<RequestInfo> treeItem = new TreeItem<>(requestInfo);
        IconGenerator.setIcon(treeItem);
        currentNode.getChildren().add(treeItem);

    }

    private void addFolder(TreeView<RequestInfo> treeView) {
        TreeItem<RequestInfo> currentNode = treeView.getSelectionModel().getSelectedItem();
        RequestInfo requestInfo = RequestGenerator.createFolder();
        requestInfo.setParent(currentNode.getValue().getId());
        TreeItem<RequestInfo> treeItem = new TreeItem<>(requestInfo);
        IconGenerator.setIcon(treeItem);
        currentNode.getChildren().add(treeItem);
    }

}
