package com.zhevakin.requester.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class SendRequesterConfig {

    private final Map<HttpMethod, SendRequester> myAutowireMap = new HashMap<>();

    public SendRequesterConfig(){}

    @Bean
    @Qualifier("senderMap")
    public Map<HttpMethod, SendRequester> mapBean(List<SendRequester> sendRequesterList) {
        for (SendRequester sendRequester : sendRequesterList) {
            myAutowireMap.put(sendRequester.getMethod(), sendRequester);
        }
        return myAutowireMap;
    }

}
