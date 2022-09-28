package com.zhevakin.requester.service.sync;

import com.zhevakin.requester.model.Environment;

import java.util.List;

public interface EnvironmentSyncService {

    List<Environment> syncEnvironments();

    Environment syncEnvironment(String idEnvironment);

    boolean saveEnvironments(List<Environment> environments);

    boolean saveEnvironment(Environment environment);

}
