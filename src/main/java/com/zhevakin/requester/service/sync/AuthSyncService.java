package com.zhevakin.requester.service.sync;

import com.zhevakin.requester.model.CurrentUser;
import com.zhevakin.requester.model.Environment;
import com.zhevakin.requester.model.RequestInfo;

import java.util.List;

public interface AuthSyncService {


    public void setUser(CurrentUser currentUser);

    public boolean testConnection();

    public boolean syncInService();

    public boolean isConnected();

    public String getToken();

    public String getTypeToken();

}
