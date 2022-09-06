package com.zhevakin.requester.front.generator;

import com.zhevakin.requester.enums.TypeRequest;
import com.zhevakin.requester.model.RequestInfo;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;

public class IconGenerator {

    private final static String FORMAT = ".png";

    private final static String RESOURCE_PROJECT = "/src/main/resources/images";
    private final static String DIR_PROJECT = System.getProperties().get("user.dir").toString() + RESOURCE_PROJECT;
    private final static String COLLECTION_IMAGE = DIR_PROJECT + "/Collection" + FORMAT;
    private final static String NONE_IMAGE = DIR_PROJECT + "/NONE" + FORMAT;
    private final static String FOLDER_IMAGE = DIR_PROJECT + "/Folder" + FORMAT;
    private final static String SUCCESS_IMAGE = DIR_PROJECT + "/Success" + FORMAT;
    private final static String FAILED_IMAGE = DIR_PROJECT + "/Failed" + FORMAT;
    private final static String REFRESH_IMAGE = DIR_PROJECT + "/Refresh" + FORMAT;


    public static void setStatusIcon(Button button, boolean isConnected) {
        String input = FAILED_IMAGE;
        if (isConnected) input = SUCCESS_IMAGE;
        setIcon(button, input);
    }

    public static void setIcon(TreeItem<RequestInfo> treeItem) {
        String input = "";
        switch (treeItem.getValue().getTypeRequest()) {
            case REQUEST: setRequestIcon(treeItem); break;
            case FOLDER: input = FOLDER_IMAGE; break;
            case COLLECTIONS: input = COLLECTION_IMAGE; break;
            default: return;
        }
        try {
            InputStream inputStream;
            if (treeItem.getValue().getTypeRequest() != TypeRequest.REQUEST) {
                File file = new File(input);
                if (!file.exists()) return;
                inputStream = new BufferedInputStream( new FileInputStream(file));
                Node icon = new ImageView(new Image(inputStream));
                treeItem.setGraphic(icon);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void setRequestIcon(TreeItem<RequestInfo> treeItem) {
        String enumName;
        if (treeItem.getValue().getRequestMethod() == null) enumName = NONE_IMAGE;
        else enumName = DIR_PROJECT + "/" + treeItem.getValue().getRequestMethod().toString() + FORMAT;
        try {
            InputStream inputStream;
            File file = new File(enumName);
            if (!file.exists()) return;
            inputStream = new BufferedInputStream( new FileInputStream(file));
            Node icon = new ImageView(new Image(inputStream));
            treeItem.setGraphic(icon);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void setRefreshIcon(Button statusButton) {

        setIcon(statusButton, REFRESH_IMAGE);

    }

    private static void setIcon(Button button, String input) {
        try {
            File file = new File(input);
            if (!file.exists()) return;
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            button.setGraphic(new ImageView(new Image(inputStream)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
