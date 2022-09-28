package com.zhevakin.requester.service.sync;

import com.zhevakin.requester.model.RequestInfo;

import java.util.List;

public interface RequestSyncService {

    List<RequestInfo> syncRequests();

    List<RequestInfo> syncCollection(String idCollection);

    boolean saveRequests(List<RequestInfo> requests);

    boolean saveCollection(List<RequestInfo> requests);

}
