package com.zhevakin.requester.front.generator;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class TextFieldGenerator {

    private TextFieldGenerator() {}

    public static void textFieldListener(String oldText, String newText, GridPane gridPane, boolean params,
                                         TextField requestTextField) {

        // TODO: пропадает фокус, разобраться в чем причина

        if (oldText.equals(newText) || !params) return ;
        StringBuilder stringBuilder = new StringBuilder();

        String url = requestTextField.getText();
        url = url.split("\\?")[0];


        ObservableList<Node> childrens = gridPane.getChildren();

        Node focus = new Node() {};
        int index = 0;
        for (Node node : childrens) {
            if (node instanceof Label) continue;
            if (index == 0) stringBuilder.append("?");
            else if (index % 2 == 0) stringBuilder.append("&");
            else stringBuilder.append("=");
            TextField textField = (TextField) node;
            stringBuilder.append(textField.getText());
            if (textField.getText().equals(newText)) focus = node;
            index++;
        }
        requestTextField.setText(url + stringBuilder.toString());
        focus.requestFocus();
    }

}
