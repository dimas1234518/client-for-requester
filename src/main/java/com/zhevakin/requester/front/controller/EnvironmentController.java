package com.zhevakin.requester.front.controller;

import com.zhevakin.requester.additional.Import;
import com.zhevakin.requester.facade.SyncFacade;
import com.zhevakin.requester.front.generator.GridGenerator;
import com.zhevakin.requester.model.Environment;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FxmlView("environment-stage.fxml")
public class EnvironmentController {

    // TODO: при создание учитывать, что id - это guid , а rowId - номер строки

    @FXML
    private ComboBox<Environment> environmentComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private GridPane variablesGrid;

    List<Environment> environmentList;
    Environment environment;

    private final GridGenerator gridGenerator;
    private final SyncFacade syncFacade;
    private final Import importer;

    @Autowired
    public EnvironmentController(GridGenerator gridGenerator, Import importer, SyncFacade syncFacade) {
        this.gridGenerator = gridGenerator;
        this.importer = importer;
        this.syncFacade = syncFacade;
    }


    private void saveEnvironments() {

        environment.setVariables(gridGenerator.getVariables(getVariablesGrid()));
        int index = environmentList.indexOf(environmentList.stream()
                                                    .filter(env -> env.getId()
                                                    .equals(environment.getId()))
                                                    .findFirst()
                                                    .orElseThrow());
        environmentList.set(index, environment);
        importer.saveEnvironments(environmentList);

        // Сохранение на сервере
        syncFacade.saveEnvironment(environment);

    }


    public void fillVariablesGrid(Environment environment) {

        gridGenerator.fillVariablesGrid(getVariablesGrid(), environment.getVariables());
        this.environment = environment;
    }

    public GridPane getVariablesGrid() {return variablesGrid;}

    public void setEnvironmentList(List<Environment> environmentList) {
        this.environmentList = environmentList;
        environmentComboBox.setItems(FXCollections.observableArrayList(environmentList));
        environmentComboBox.setValue(this.environmentList.stream()
                                    .filter(env -> env.getId()
                                    .equals(environment.getId()))
                                    .findFirst()
                                    .orElseThrow());
    }

    public void save(ActionEvent actionEvent) {
        saveEnvironments();
    }

    public void changeValue(ActionEvent actionEvent) {

        Environment newEnvironment = environmentComboBox.getSelectionModel().getSelectedItem();

        if (newEnvironment != null && !environment.getId().equals(newEnvironment.getId())) {
            environment = newEnvironment;
        }
        fillVariablesGrid(environment);

    }

}
