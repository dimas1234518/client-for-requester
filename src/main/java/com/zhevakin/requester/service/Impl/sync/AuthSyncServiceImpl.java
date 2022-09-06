package com.zhevakin.requester.service.Impl.sync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhevakin.requester.model.Answer;
import com.zhevakin.requester.model.CurrentUser;
import com.zhevakin.requester.model.domain.JwtResponse;
import com.zhevakin.requester.sender.Sender;
import com.zhevakin.requester.service.sync.AuthSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthSyncServiceImpl implements AuthSyncService {

    private String authToken;
    private String typeToken;

    private boolean isConnected;

    private String server = "";
    private final String API = "api/auth";

    private CurrentUser currentUser;

    private final Sender sender;

    @Autowired
    public AuthSyncServiceImpl(
            @Value("${service.server}") String server,
            Sender sender) {
        this.server = server;
        this.sender = sender;
    }

    @Override
    public boolean syncInService() {
        //TODO: переделать дописать bCryptPasswordEncoder.encode
        try {
            long timestamp = System.currentTimeMillis();
            Answer answer = firstStep(timestamp);
            long timestampFromServer = Long.parseLong(answer.getBody());
            currentUser.setPassword(String.valueOf(timestampFromServer - timestamp));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(currentUser);
            answer = secondStep(json);
            gson = new GsonBuilder().create();
            JwtResponse response = gson.fromJson(answer.getBody(), JwtResponse.class);
            authToken = response.getAccessToken();
            typeToken = response.getType();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    private Answer firstStep(long timestamp) {
        String method = "/";
        Map<String, String> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        headers.put("user", currentUser.getCurrentUser());
        params.put("timestamp", String.valueOf(timestamp));
        return sender.send(server + API + method, headers, params, HttpMethod.GET, "");
    }

    private Answer secondStep(String json) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", MediaType.APPLICATION_JSON_VALUE);
        Map<String, String> params = new HashMap<>();
        String method = "/login";
        return sender.send(server + API + method, headers, params, HttpMethod.POST, json);
    }

    @Override
    public void setUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public boolean testConnection() {

        String method = "check";
        Map<String, String> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        Answer answer = sender.send(server + method, headers, params, HttpMethod.GET, "");

        isConnected = answer.getHttpStatus() == HttpStatus.OK;

        return isConnected;

    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public String getToken() {
        return authToken;
    }

    @Override
    public String getTypeToken() {
        return typeToken;
    }
}
