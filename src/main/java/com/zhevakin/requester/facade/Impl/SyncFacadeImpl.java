package com.zhevakin.requester.facade.Impl;

import com.zhevakin.requester.facade.SyncFacade;
import com.zhevakin.requester.model.CurrentUser;
import com.zhevakin.requester.model.Environment;
import com.zhevakin.requester.model.RequestInfo;
import com.zhevakin.requester.service.sync.AuthSyncService;
import com.zhevakin.requester.service.sync.EnvironmentSyncService;
import com.zhevakin.requester.service.sync.RequestSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SyncFacadeImpl implements SyncFacade {


    private final AuthSyncService authSyncService;

    private final RequestSyncService requestSyncService;

    private final EnvironmentSyncService environmentSyncService;

    @Autowired
    public SyncFacadeImpl(AuthSyncService authSyncService, RequestSyncService requestSyncService, EnvironmentSyncService environmentSyncService) {
        this.authSyncService = authSyncService;
        this.requestSyncService = requestSyncService;
        this.environmentSyncService = environmentSyncService;
    }

    @Override
    public void setUser(CurrentUser currentUser) {
        authSyncService.setUser(currentUser);
    }

    @Override
    public boolean testConnection() {
        return authSyncService.testConnection();
    }

    @Override
    public boolean isConnected() {
        return authSyncService.isConnected();
    }

    @Override
    public String getToken() {
        return authSyncService.getToken();
    }

    @Override
    public String getTypeToken() {
        return authSyncService.getTypeToken();
    }

    @Override
    public boolean syncInService() {
        return authSyncService.syncInService();
    }

    @Override
    public List<Environment> syncEnvironments() {
        return environmentSyncService.syncEnvironments();
    }

    @Override
    public Environment syncEnvironment(String idEnvironment) {
        return environmentSyncService.syncEnvironment(idEnvironment);
    }

    @Override
    public boolean saveEnvironments(List<Environment> environments) {
        return environmentSyncService.saveEnvironments(environments);
    }

    @Override
    public boolean saveEnvironment(Environment environment) {
        return environmentSyncService.saveEnvironment(environment);
    }

    @Override
    public List<RequestInfo> syncRequests() {
        return requestSyncService.syncRequests();
    }

    @Override
    public List<RequestInfo> syncCollection(String idCollection) {
        return requestSyncService.syncCollection(idCollection);
    }

    @Override
    public boolean saveRequests(List<RequestInfo> requests) {
        return requestSyncService.saveRequests(requests);
    }

    @Override
    public boolean saveCollection(List<RequestInfo> requests) {
        return requestSyncService.saveCollection(requests);
    }
}
