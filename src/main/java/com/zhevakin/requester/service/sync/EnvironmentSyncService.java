package com.zhevakin.requester.service.sync;

import com.zhevakin.requester.model.Environment;

import java.util.List;

public interface EnvironmentSyncService {

    public List<Environment> syncEnvironments();

    public Environment syncEnvironment(String idEnvironment);

    public boolean saveEnvironments(List<Environment> environments);

    public boolean saveEnvironment(Environment environment);

}
