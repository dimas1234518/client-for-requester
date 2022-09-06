package com.zhevakin.requester.additional;

import com.google.gson.Gson;

public class JsonConverter {

    public static <Model> Model getObjectFromJson(String data, Class<Model> className) {
        Gson gson = new Gson();
        return gson.fromJson(data, className);
    }

    public static <Model> String getJsonFromObject(Object obj, Class<Model> className) {
        Gson gson = new Gson();
        return gson.toJson(obj, className);
    }

}
