package com.zhevakin.requester.additional.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.zhevakin.requester.additional.Import;
import com.zhevakin.requester.model.Environment;
import com.zhevakin.requester.model.RequestInfo;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ImportImpl implements Import {


    private final String resourceProject = "/src/main/resources";
    private final String dirProject = System.getProperties().get("user.dir").toString() + resourceProject;
    private final String requestFile = "/requests.json";
    private final String environmentFile = "/environments.json";


    @Override
    public void saveRequest(RequestInfo requestInfo) {

    }

    @Override
    public void saveRequests(List<RequestInfo> requests) {

        String path = dirProject + requestFile;
        saveRequestToFile(requests, path);

    }

    private void saveRequestToFile(List<RequestInfo> requests, String path) {
        List<RequestInfo> saveRequests = new ArrayList<>();
        for (RequestInfo request : requests) {
            int index = findDuplicate(saveRequests, request);
            if (index == -1) saveRequests.add(request);
            else saveRequests.set(index, request);
        }
        try {
            if (Files.notExists(Paths.get(path))) Files.createFile(Paths.get(path));
            try (FileWriter file = new FileWriter(path)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(saveRequests);
                file.write(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveRequests(List<RequestInfo> requests, String path) {

        saveRequestToFile(requests, path);

    }

    @Override
    public void saveEnvironment(Environment environment, String path) {

        try {
            if (Files.notExists(Paths.get(path))) Files.createFile(Paths.get(path));
            try (FileWriter file = new FileWriter(path)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(environment);
                file.write(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveEnvironment(Environment environment) {

    }

    @Override
    public void saveEnvironments(List<Environment> environments) {
        String path = dirProject + environmentFile;
        try {
            if (Files.notExists(Paths.get(path))) Files.createFile(Paths.get(path));
            try (FileWriter file = new FileWriter(path)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(environments);
                file.write(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<RequestInfo> loadRequests() {
        String path = dirProject + requestFile;
        return getRequestInfo(path);
    }

    @Override
    public List<Environment> loadEnvironments() {
        String path = dirProject + environmentFile;
        return getEnvironments(path);
    }

    private List<Environment> getEnvironments(String path) {
        List<Environment> environments = new ArrayList<>();
        if (Files.notExists(Paths.get(path))) return environments;
        try {
            Gson gson = new GsonBuilder().create();
            JsonReader reader = new JsonReader(new FileReader(path));
            Type type = new TypeToken<List<Environment>>() {
            }.getType();
            environments = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return environments;
    }

    @Override
    public List<RequestInfo> loadRequests(String path) {
        return getRequestInfo(path);
    }

    private List<RequestInfo> getRequestInfo(String path) {
        List<RequestInfo> requests = new ArrayList<>();
        if (Files.notExists(Paths.get(path))) return requests;
        try {
            Gson gson = new GsonBuilder().create();
            JsonReader reader = new JsonReader(new FileReader(path));
            requests = Arrays.asList(gson.fromJson(reader, RequestInfo[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public List<Environment> loadEnvironments(String path) {
        return getEnvironments(path);
    }


    //TODO: Переделать на поиск дублей
    private int findDuplicate(List<RequestInfo> requests, RequestInfo requestInfo) {
        try {
            var request = requests.stream()
                    .filter(r -> r.getId().equals(requestInfo.getId()))
                    .findFirst()
                    .orElse(null);
            if (request == null) return -1;

            return requests.indexOf(request);
        } catch (NullPointerException ignored) {
            return -1;
        }
    }

}
