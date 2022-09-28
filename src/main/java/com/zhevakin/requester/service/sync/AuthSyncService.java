package com.zhevakin.requester.service.sync;

import com.zhevakin.requester.model.CurrentUser;


public interface AuthSyncService {


    void setUser(CurrentUser currentUser);

    boolean testConnection();

    boolean syncInService();

    boolean isConnected();

    String getToken();

    String getTypeToken();

}
