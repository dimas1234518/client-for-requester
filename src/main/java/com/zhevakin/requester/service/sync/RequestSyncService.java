package com.zhevakin.requester.service.sync;

import com.zhevakin.requester.model.RequestInfo;

import java.util.List;

public interface RequestSyncService {

    public List<RequestInfo> syncRequests();

    public List<RequestInfo> syncCollection(String idCollection);

    public boolean saveRequests(List<RequestInfo> requests);

    public boolean saveCollection(List<RequestInfo> requests);

}
