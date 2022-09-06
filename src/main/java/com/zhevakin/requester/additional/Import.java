package com.zhevakin.requester.additional;

import com.zhevakin.requester.model.Environment;
import com.zhevakin.requester.model.RequestInfo;

import java.util.List;

public interface Import {

    void saveRequest(RequestInfo requestInfo);

    void saveRequests(List<RequestInfo> requests, String path);

    void saveRequests(List<RequestInfo> requests);

    void saveEnvironment(Environment environment);

    void saveEnvironments(List<Environment> environments);

    void saveEnvironment(Environment environment, String path);

    List<RequestInfo> loadRequests();

    List<RequestInfo> loadRequests(String path);

    List<Environment> loadEnvironments();

    List<Environment> loadEnvironments(String path);


}
