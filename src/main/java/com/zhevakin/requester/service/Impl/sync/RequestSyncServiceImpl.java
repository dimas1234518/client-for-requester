package com.zhevakin.requester.service.Impl.sync;

import com.zhevakin.requester.additional.JsonConverter;
import com.zhevakin.requester.model.Answer;
import com.zhevakin.requester.model.RequestInfo;
import com.zhevakin.requester.sender.Sender;
import com.zhevakin.requester.service.sync.AuthSyncService;
import com.zhevakin.requester.service.sync.RequestSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RequestSyncServiceImpl implements RequestSyncService {

    private final String API = "api/requests";

    private String server = "";

    private final AuthSyncService authSyncService;
    private final Sender sender;

    @Autowired
    public RequestSyncServiceImpl(AuthSyncService authSyncService, Sender sender, @Value("${service.server}") String server) {
        this.authSyncService = authSyncService;
        this.sender = sender;
        this.server = server;
    }

    @Override
    public List<RequestInfo> syncRequests() {

        String method = "/";
        Map<String,String> headers = new HashMap<>();
        Map<String,String> params  = new HashMap<>();
        headers.put("Authorization", authSyncService.getTypeToken() + " " + authSyncService.getToken());
        Answer answer = sender.send(server + API + method, headers, params, HttpMethod.GET, "");

        if (answer.getHttpStatus() == HttpStatus.FORBIDDEN) {
            authSyncService.syncInService();
            headers.replace("Authorization", authSyncService.getTypeToken() + " " + authSyncService.getToken());
            answer = sender.send(server + API, headers, params, HttpMethod.GET, "");
            if (answer.getHttpStatus() != HttpStatus.OK) return null;
        }
        if (answer.getHttpStatus() == HttpStatus.OK) return Arrays.asList(JsonConverter
                .getObjectFromJson(answer.getBody(), RequestInfo[].class));
        return null;
    }

    @Override
    public List<RequestInfo> syncCollection(String idCollection) {
        String method = "/";
        Map<String,String> headers = new HashMap<>();
        Map<String,String> params  = new HashMap<>();

        headers.put("Authorization", authSyncService.getTypeToken() + " " + authSyncService.getToken());

        Answer answer = sender.send(server + API + method + idCollection, headers, params, HttpMethod.GET, "");

        if (answer.getHttpStatus() == HttpStatus.FORBIDDEN) {
            authSyncService.syncInService();
            headers.replace("Authorization", authSyncService.getTypeToken() + " " + authSyncService.getToken());
            answer = sender.send(server + API + method + idCollection, headers, params, HttpMethod.GET, "");
            if (answer.getHttpStatus() != HttpStatus.OK) return null;
        }
        if (answer.getHttpStatus() == HttpStatus.OK) return Arrays.asList(JsonConverter.getObjectFromJson(answer.getBody(),
                                                                RequestInfo[].class));
        return null;
    }

    @Override
    public boolean saveRequests(List<RequestInfo> requests) {
        return sendSaveRequests(requests);
    }

    @Override
    public boolean saveCollection(List<RequestInfo> requests) {

        return sendSaveRequests(requests);

    }

    private boolean sendSaveRequests(List<RequestInfo> requests) {
        String method = "/";
        Map<String,String> headers = new HashMap<>();
        Map<String,String> params  = new HashMap<>();

        headers.put("Authorization", authSyncService.getTypeToken() + " " + authSyncService.getToken());

        Answer answer = sender.send(server + API + method, headers, params, HttpMethod.POST, "");

        if (answer.getHttpStatus() == HttpStatus.FORBIDDEN) {
            authSyncService.syncInService();
            headers.replace("Authorization", authSyncService.getTypeToken() + " " + authSyncService.getToken());
            answer = sender.send(server + API, headers, params, HttpMethod.POST,
                    JsonConverter.getJsonFromObject(requests, RequestInfo[].class));
            if (answer.getHttpStatus() != HttpStatus.OK) return false;
        }
        return answer.getHttpStatus() == HttpStatus.OK;
    }
}
