package com.zhevakin.requester.front.controller;

import com.zhevakin.requester.additional.Import;
import com.zhevakin.requester.enums.TextMode;
import com.zhevakin.requester.enums.TypeRequest;
import com.zhevakin.requester.facade.SyncFacade;
import com.zhevakin.requester.front.generator.GridGenerator;
import com.zhevakin.requester.front.generator.IconGenerator;
import com.zhevakin.requester.front.generator.RequestGenerator;
import com.zhevakin.requester.front.generator.TreeGenerator;
import com.zhevakin.requester.model.Answer;
import com.zhevakin.requester.model.CurrentUser;
import com.zhevakin.requester.model.Environment;
import com.zhevakin.requester.model.RequestInfo;
import com.zhevakin.requester.sender.Sender;
import com.zhevakin.requester.service.PropertyService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
@FxmlView("main-stage.fxml")
public class MainController {

    @FXML
    private Tab authTab;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab paramsTab;
    @FXML
    private Tab headersTab;

    @FXML
    private Tab bodyTab;

    @FXML
    private Button statusButton;
    @FXML
    private MenuItem importMenuItem;
    @FXML
    private Menu fileMenu;
    @FXML
    private ComboBox<TextMode> requestTextModeComboBox;
    @FXML
    private ComboBox<TextMode> responseTextModeComboBox;

    @FXML
    private Label currentUserLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Button showEnvironmentButton;
    @FXML
    private TreeView<RequestInfo> requestTree;
    @FXML
    private TextArea requestBody;
    @FXML
    private TextArea responseBody;
    @FXML
    private GridPane paramsGrid;
    @FXML
    private GridPane headersGrid;
    @FXML
    private ComboBox<HttpMethod> methodComboBox;
    @FXML
    private TextField requestTextField;
    @FXML
    private Label statusLabel;
    @FXML
    private Label responseLabel;

    @FXML
    private ComboBox<Environment> environmentComboBox;

    @FXML
    private Button sendButton;

    List<RequestInfo> requests;
    List<Environment> environments;
    List<Stage> openStages = new ArrayList<>();
    List<Tab> tabs = new ArrayList<>();

    private final Sender sender;
    private final Import importer;
    private final SyncFacade syncFacade;
    private EnvironmentController environmentController;
    private final PropertyService propertyService;
    private final TreeGenerator treeGenerator;
    private final GridGenerator gridGenerator;
    private FxWeaver fxWeaver;
    private final String ENVIRONMENTS = "Environments";
    private RequestInfo currentRequestInfo;

    public void setFxWeaver(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    public void setEnvironmentController(EnvironmentController environmentController) {
        this.environmentController = environmentController;
    }

    @Autowired
    public MainController(Sender sender, Import importer, SyncFacade syncFacade, PropertyService propertyService,
                          TreeGenerator treeGenerator, GridGenerator gridGenerator) {
        this.sender = sender;
        this.importer = importer;
        this.syncFacade = syncFacade;
        this.propertyService = propertyService;
        this.treeGenerator = treeGenerator;
        this.gridGenerator = gridGenerator;
        requests = new ArrayList<>();
        environments = new ArrayList<>();
    }

    public void send(ActionEvent actionEvent) {

        Map<String, String> params;
        Map<String, String> headers;

        params = gridGenerator.parseGrid(paramsGrid);
        headers = gridGenerator.parseGrid(headersGrid);

        Answer answer = sender.send(requestTextField.getText(), headers, params, methodComboBox.getValue(),
                requestBody.getText());
        statusLabel.setText(answer.getHttpStatus().toString());
        responseBody.setText(answer.getBody() + "\n" + answer.getHeaders().toString());

    }




    public void showEnvironments(ActionEvent actionEvent) {

        Stage findStage = openStages.stream()
                .filter(n -> n.getTitle().equals(ENVIRONMENTS))
                .findFirst().orElseThrow();
        if (findStage.isShowing()) {
            findStage.toFront();
        } else {
            findStage.show();
            environmentController.fillVariablesGrid(environmentComboBox.getValue());
            environmentController.setEnvironmentList(environments);
        }
    }

    public void start() {

        tabs.add(authTab);
        tabs.add(paramsTab);
        tabs.add(headersTab);
        tabs.add(bodyTab);

        statusButton.setOnMouseEntered(mouseEvent -> { IconGenerator.setRefreshIcon(statusButton); });
        statusButton.setOnMouseExited(mouseEvent -> {IconGenerator.setStatusIcon(statusButton, syncFacade.isConnected());});

        CurrentUser currentUser = new CurrentUser(System.getProperty("user.name"));
        String domain = System.getenv("USERDOMAIN");
        if (domain != null) currentUser.setDomain(domain);
        currentUserLabel.setText(currentUser.toString());
        IconGenerator.setStatusIcon(statusButton, syncFacade.testConnection());

        syncFacade.setUser(currentUser);

        // Заполнение дерева
        requests = importer.loadRequests();
        if (requests.size() != 0) setCurrentRequest(requests.get(0));
        environments = importer.loadEnvironments();
        treeGenerator.fillTree(requestTree, requests, tabs, mainTabPane);
        syncFacade.saveRequests(requests);
        //Заполнение ComboBox
        environmentComboBox.setItems(FXCollections.observableArrayList(environments));
        environmentComboBox.setValue(environments.get(0));
        syncFacade.syncEnvironment(environmentComboBox.getSelectionModel().getSelectedItem().getId());
        syncFacade.syncRequests();
        syncFacade.syncCollection(requests.stream().filter(r -> r.getTypeRequest() == TypeRequest.COLLECTIONS).findFirst().orElse(null).getId());
        requestTextModeComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(TextMode.values())));
        requestTextModeComboBox.setValue(TextMode.NONE);
        responseTextModeComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(TextMode.values())));
        responseTextModeComboBox.setValue(TextMode.NONE);


        // Размеры задать
        Parent root = fxWeaver.loadView(environmentController.getClass());
        Scene scene = new Scene(root, 800, 600);
        //Scene scene = new Scene();
        Stage environmentStage = new Stage();
        environmentStage.setScene(scene);
        environmentStage.setTitle(ENVIRONMENTS);
        openStages.add(environmentStage);


    }

    public void setCurrentRequest(RequestInfo requestInfo) {

        this.currentRequestInfo = requestInfo;
        nameLabel.setText(requestInfo.getName());

        switch (requestInfo.getTypeRequest()) {
            case COLLECTIONS:
            case FOLDER: {
                if (mainTabPane.getTabs().size() != 1) {
                    for (int i = 1; i < tabs.size(); i++) mainTabPane.getTabs().remove(1);
                }

                methodComboBox.setVisible(false);
                requestTextField.setVisible(false);
                sendButton.setVisible(false);

                break;
            }
            case REQUEST: {
                if (mainTabPane.getTabs().size() == 1) {
                    for (int i = 1; i < tabs.size(); i++) {
                        mainTabPane.getTabs().add(tabs.get(i));
                    }
                }
                methodComboBox.setVisible(true);
                methodComboBox.setValue(currentRequestInfo.getRequestMethod());
                requestTextField.setVisible(true);
                if (currentRequestInfo.getRequest() != null) requestTextField.setText(currentRequestInfo.getRequest());
                sendButton.setVisible(true);

                if (currentRequestInfo.getTypeBody() != null) requestTextModeComboBox.setValue(currentRequestInfo.getTypeBody());
                else requestTextModeComboBox.setValue(TextMode.NONE);

                if (currentRequestInfo.getTypeResponseBody() != null) responseTextModeComboBox.setValue(currentRequestInfo.getTypeResponseBody());
                else responseTextModeComboBox.setValue(TextMode.NONE);
                requestBody.setText(currentRequestInfo.getBody());


                gridGenerator.fillHeadersGrid(requestInfo.getHeaders(), headersGrid);

                break;
            }
        }



    }

    public ComboBox<HttpMethod> getMethodComboBox() {
        return methodComboBox;
    }

    public TextField getRequestTextField() {
        return requestTextField;
    }

    public Button getSendButton() {
        return sendButton;
    }

    public GridPane getParamsGrid() {
        return paramsGrid;
    }

    public GridPane getHeadersGrid() {
        return headersGrid;
    }

    public TreeView<RequestInfo> getRequestTree() {
        return requestTree;
    }

    public void makeImport(ActionEvent actionEvent) {

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Collection");
        File selectedFile = fileChooser.showOpenDialog(requestTree.getScene().getWindow());
        if (selectedFile != null) {
            List<RequestInfo> requests = new ArrayList<>();
            requests = importer.loadRequests(selectedFile.getPath());
            TreeItem<RequestInfo> root = new TreeItem<>(requests.stream()
                    .filter(request -> request.getTypeRequest() == TypeRequest.COLLECTIONS)
                    .findFirst()
                    .orElseThrow());
            IconGenerator.setIcon(root);
            for (RequestInfo request : requests) {
                treeGenerator.treeParser(root, request);
            }
            requestTree.getRoot().getChildren().add(root);
        }
    }

    public void refreshStatus(ActionEvent actionEvent) {

        String refresh = "refreshAll";

        if (propertyService.getProperty(refresh) == null) {
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Refresh Settings");
            alert.setHeaderText("Do you want refresh all content (requests, environments)?");
            alert.setContentText("Press Yes for accepted, or No for cancel");
            alert.getButtonTypes().setAll(okButton, noButton);

            Optional<ButtonType> optional = alert.showAndWait();

            optional.get();
            propertyService.setProperty(refresh, optional.get() != noButton);

        }
        boolean refreshAll = Boolean.parseBoolean(propertyService.getProperty("refreshAll"));
        syncFacade.testConnection();

        //TODO: продумать сохранение по последней дате редактирования. Возможно на сервере старые данные и не нужно перетирать свои, возможно нужен параметр
        if (refreshAll) {
            requests = syncFacade.syncRequests();
            environments = syncFacade.syncEnvironments();
            importer.saveEnvironments(environments);
            importer.saveRequests(requests);
            treeGenerator.fillTree(requestTree, requests, tabs, mainTabPane);
        }
    }
}
