package com.zhevakin.requester.front.generator;

import com.zhevakin.requester.front.controller.MainController;
import com.zhevakin.requester.model.Variable;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import org.springframework.stereotype.Component;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhevakin.requester.front.generator.TextFieldGenerator.textFieldListener;

// TODO: продумать как создать две пустые строки если хотя бы одна ячейка заполнена

@Component
public class GridGenerator {

    public ColumnConstraints getColumnConstraint(int width) {
        ColumnConstraints column = new ColumnConstraints(width);
        column.setHgrow(Priority.ALWAYS);
        return column;
    }

    public ColumnConstraints getColumnConstraint() {
        ColumnConstraints column = new ColumnConstraints(100);
        column.setHgrow(Priority.ALWAYS);
        return column;
    }

    public RowConstraints getRowConstraints() {
        return new RowConstraints(20, 20, 20);
    }

    public Label createHeaderNode(String text) {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setStyle("-fx-border-style: solid; -fx-background-color: white;");
        return label;
    }


    /***
     *
     * @param text in TextField
     * @param params True - if paramsGrid, else headersGrid
     * @param gridPane paramsGrid or headersGrid
     * @return TextField
     */
    public TextField createCellNode(String text, GridPane gridPane, boolean params, MainController controller) {
        TextField textField = new TextField(text);
        textField.setMaxWidth(Double.MAX_VALUE);
        textField.setStyle("-fx-border-style: solid; -fx-background-color: white;");
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
           textFieldListener(oldValue, newValue, gridPane, params, controller.getRequestTextField());
        });

        return textField;

    }

    public TextField createCellNode(String text) {
        TextField textField = new TextField(text);
        textField.setMaxWidth(Double.MAX_VALUE);
        textField.setStyle("-fx-border-style: solid; -fx-background-color: white;");

        return textField;

    }

    public void fillVariablesGrid(GridPane gridPane, List<Variable> variableList) {

        String[] headers = new String[]{"Variable", "Value"};
        gridPane.getChildren().clear();
        createGrid(headers, gridPane);

        int index = 1;

        for (Variable variable : variableList) {
            Node[] nodes = new Node[2];
            nodes[0] = createCellNode(variable.getName());
            nodes[1] = createCellNode(variable.getValue());
            gridPane.getRowConstraints().add(getRowConstraints());
            for (int j = 0; j < nodes.length; j++) {
                gridPane.add(nodes[j], j, index);
            }
            index++;
        }

    }

    public void createGrid(String[] headers, GridPane gridPane) {

        Node[] nodes = new Node[headers.length];
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().add(getRowConstraints());
        for (int i = 0; i < headers.length; i++) {
            nodes[i] = createHeaderNode(headers[i]);
            gridPane.add(nodes[i], i, 0);
        }
    }

    public void fillHeadersGrid(Map<String,String> headers, GridPane gridPane) {

        String[] headersGrid = new String[]{"Key", "Value"};
        createGrid(headersGrid, gridPane);

        int index = 1;
        if (headers == null ) return;
        for (Map.Entry<String,String> pair : headers.entrySet()) {
            Node[] nodes = new Node[2];
            nodes[0] = createCellNode(pair.getKey());
            nodes[1] = createCellNode(pair.getValue());

            for (int j = 0; j < nodes.length; j++) {
                gridPane.add(nodes[j], j, index);
            }

            index++;
        }

    }

    public void fillParamsGrid(String oldUrl, String url, MainController mainController) {

        GridPane gridPane = mainController.getParamsGrid();

        if (oldUrl.equals(url)) return;

        String[] headers = new String[]{"Key", "Value"};
        createGrid(headers, gridPane);

        int index = 1;

        String[] splitUrl = url.split("\\?");
        if (splitUrl.length < 2) return;

        String[] inputParams = splitUrl[1].split("&");
        for (String input : inputParams) {
            Node[] nodes = new Node[2];

            String[] inputString = input.split("=");
            int i = 0;

            while (i < inputString.length) {
                nodes[i] = createCellNode(inputString[i], gridPane, true, mainController);
                i++;
                if (i == 2) break;
            }

            if (inputString.length == 1) nodes[1] = createCellNode("", gridPane, true, mainController);

            gridPane.getRowConstraints().add(getRowConstraints());
            for (int j = 0; j < nodes.length; j++) {
                gridPane.add(nodes[j], j, index);
            }

            index++;

        }

    }

    public List<Variable> getVariables(GridPane gridPane) {

        ObservableList<Node> childrens = gridPane.getChildren();

        List<Variable> variables = new ArrayList<>();

        int index = 1;
        for (int i = 0; i < childrens.size(); i+=2) {
            if (childrens.get(i) instanceof Label) continue;
            Variable variable = new Variable(String.valueOf(index));
            TextField nameField = (TextField) childrens.get(i);
            TextField valueField = (TextField) childrens.get(i+1);
            variable.setName(nameField.getText());
            variable.setValue(valueField.getText());
            variables.add(variable);
            index++;
        }
        return variables;
    }

    public Map<String, String> parseGrid(GridPane gridPane) {

        Map<String, String> parseMap = new HashMap<>();
        String key = "";
        String value = "";
        TextField currentNode;
        int index = 0;

        for (Node node : gridPane.getChildren()) {
            if (node instanceof Label) continue;
            currentNode = (TextField) node;
            if (index % 2 == 0)  {
                key = currentNode.getText();
            } else if (index % 2 == 1) {
                value = currentNode.getText();
                parseMap.put(key,value);
            }
            index ++;
        }

        return parseMap;

    }
}


