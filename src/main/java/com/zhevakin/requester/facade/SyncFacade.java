package com.zhevakin.requester.facade;

import com.zhevakin.requester.model.CurrentUser;
import com.zhevakin.requester.model.Environment;
import com.zhevakin.requester.model.RequestInfo;

import java.util.List;

public interface SyncFacade {

    public void setUser(CurrentUser currentUser);

    public boolean testConnection();

    public boolean isConnected();

    public String getToken();

    public String getTypeToken();

    public boolean syncInService();

    public List<Environment> syncEnvironments();

    public Environment syncEnvironment(String idEnvironment);

    public boolean saveEnvironments(List<Environment> environments);

    public boolean saveEnvironment(Environment environment);

    public List<RequestInfo> syncRequests();

    public List<RequestInfo> syncCollection(String idCollection);

    public boolean saveRequests(List<RequestInfo> requests);

    public boolean saveCollection(List<RequestInfo> requests);

}
