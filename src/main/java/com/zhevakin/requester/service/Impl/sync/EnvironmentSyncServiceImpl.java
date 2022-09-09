package com.zhevakin.requester.service.Impl.sync;

import com.zhevakin.requester.additional.JsonConverter;
import com.zhevakin.requester.model.Answer;
import com.zhevakin.requester.model.Environment;
import com.zhevakin.requester.sender.Sender;
import com.zhevakin.requester.service.sync.AuthSyncService;
import com.zhevakin.requester.service.sync.EnvironmentSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EnvironmentSyncServiceImpl implements EnvironmentSyncService {

    private final String API = "api/environments";
    private String server = "";

    private final AuthSyncService authSyncService;
    private final Sender sender;

    @Autowired
    public EnvironmentSyncServiceImpl(AuthSyncService authSyncService, Sender sender, @Value("${service.server}") String server) {
        this.authSyncService = authSyncService;
        this.sender = sender;
        this.server = server;
    }

    @Override
    public List<Environment> syncEnvironments() {

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
                                                            .getObjectFromJson(answer.getBody(), Environment[].class));
        return null;
    }

    @Override
    public Environment syncEnvironment(String idEnvironment) {

        String method = "/";
        Map<String,String> headers = new HashMap<>();
        Map<String,String> params  = new HashMap<>();

        headers.put("Authorization", authSyncService.getTypeToken() + " " + authSyncService.getToken());

        Answer answer = sender.send(server + API + method + idEnvironment, headers, params, HttpMethod.GET, "");

        if (answer.getHttpStatus() == HttpStatus.FORBIDDEN) {
            authSyncService.syncInService();
            headers.replace("Authorization", authSyncService.getTypeToken() + " " + authSyncService.getToken());
            answer = sender.send(server + API + method + idEnvironment, headers, params, HttpMethod.GET, "");
            if (answer.getHttpStatus() != HttpStatus.OK) return null;
        }
        if (answer.getHttpStatus() == HttpStatus.OK) return JsonConverter.getObjectFromJson(answer.getBody(), Environment.class);
        return null;
    }

    @Override
    public boolean saveEnvironments(List<Environment> environments) {

        String method = "/";
        Map<String,String> headers = new HashMap<>();
        Map<String,String> params  = new HashMap<>();
        String body = JsonConverter.getJsonFromObject(environments);
        headers.put("Authorization", authSyncService.getTypeToken() + " " + authSyncService.getToken());

        Answer answer = sender.send(server + API + method, headers, params, HttpMethod.POST, body);

        if (answer.getHttpStatus() == HttpStatus.FORBIDDEN) {
            authSyncService.syncInService();
            headers.replace("Authorization", authSyncService.getTypeToken() + " " + authSyncService.getToken());
            answer = sender.send(server + API, headers, params, HttpMethod.POST, body);
            if (answer.getHttpStatus() != HttpStatus.OK) return false;
        }
        return answer.getHttpStatus() == HttpStatus.OK;
    }

    @Override
    public boolean saveEnvironment(Environment environment) {
        String method = "/";
        Map<String,String> headers = new HashMap<>();
        Map<String,String> params  = new HashMap<>();

        List<Environment> environments = new ArrayList<>();
        environments.add(environment);
        String body = JsonConverter.getJsonFromObject(environments);
        headers.put("Authorization", authSyncService.getTypeToken() + " " + authSyncService.getToken());

        Answer answer = sender.send(server + API + method, headers, params, HttpMethod.POST, body);

        if (answer.getHttpStatus() == HttpStatus.FORBIDDEN) {
            authSyncService.syncInService();
            headers.replace("Authorization", authSyncService.getTypeToken() + " " + authSyncService.getToken());
            answer = sender.send(server + API, headers, params, HttpMethod.POST, body);
            if (answer.getHttpStatus() != HttpStatus.OK) return false;
        }
        return answer.getHttpStatus() == HttpStatus.OK;
    }
}
