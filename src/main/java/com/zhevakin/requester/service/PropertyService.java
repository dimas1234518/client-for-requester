package com.zhevakin.requester.service;

import java.io.IOException;

public interface PropertyService {

    void init() throws IOException;

    <object> void addProperty(String key, object value) throws IOException;

    void deleteProperty(String key);

    <object> void setProperty(String key, object value);

    String getProperty(String key);

    void saveProperties() throws IOException;

}
