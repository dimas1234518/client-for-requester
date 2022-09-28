package com.zhevakin.requester.service.Impl;

import com.zhevakin.requester.service.PropertyService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Component
public class PropertyServiceImpl implements PropertyService  {

    private static final String resourceProject = "/src/main/resources";
    private static final String dirProject = System.getProperties().get("user.dir").toString() + resourceProject;
    private static final String propertyFile = "/user.properties";
    private static final String PATH = dirProject + propertyFile;
    private Properties properties;


    @PostConstruct
    @Override
    public void init() throws IOException {
        properties = new Properties();
        if (Files.notExists(Paths.get(PATH))) Files.createFile(Paths.get(PATH));
        properties.load(new FileInputStream(PATH));
    }

    @Override
    public <object> void addProperty(String key, object value) {
        setProperty(key, value);
    }

    @Override
    public void deleteProperty(String key) {
        properties.remove(key);
    }

    @Override
    public <object> void setProperty(String key, object value) {
        properties.setProperty(key, String.valueOf(value));
    }

    @Override
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    @PreDestroy
    @Override
    public void saveProperties() throws IOException {
        properties.store(new FileWriter(PATH), "User properties");
    }
}
