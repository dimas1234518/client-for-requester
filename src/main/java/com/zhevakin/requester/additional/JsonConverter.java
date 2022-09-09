package com.zhevakin.requester.additional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class JsonConverter {

    public static <Model> Model getObjectFromJson(String data, Class<Model> className) {
        Gson gson = new Gson();
        return gson.fromJson(data, className);
    }

    public static <Model> String getJsonFromObject(Object obj) {
        Gson gson = new Gson();

        Type founderType = new TypeToken<ArrayList<Model>>(){}.getType();
        return gson.toJson(obj, founderType);
    }



}
