package com.zhevakin.requester.front.init;

import com.zhevakin.requester.front.controller.EnvironmentController;
import com.zhevakin.requester.front.controller.MainController;
import com.zhevakin.requester.front.generator.GridGenerator;
import com.zhevakin.requester.front.generator.TreeGenerator;
import javafx.collections.FXCollections;
import org.springframework.http.HttpMethod;

import java.util.Arrays;

public class InitController {

    private InitController() {}

    public static void start(MainController controller, GridGenerator gridGenerator) {

        controller.getMethodComboBox().setItems(FXCollections.observableArrayList(Arrays.asList(HttpMethod.values())));
        controller.getMethodComboBox().setValue(HttpMethod.GET);

        String[] headers = new String[] {"Key", "Value"};
        gridGenerator.createGrid(headers, controller.getParamsGrid());
        gridGenerator.createGrid(headers, controller.getHeadersGrid());
        gridGenerator.addGridEvent(controller.getParamsGrid(), controller.getRequestTextField());
        gridGenerator.addGridEvent(controller.getHeadersGrid(),controller.getRequestTextField());
        controller.getRequestTextField().textProperty().addListener((observable, oldValue, newValue) ->
           gridGenerator.fillParamsGrid(oldValue, newValue, controller)
        );
        TreeGenerator.createTree(controller);
    }

}
