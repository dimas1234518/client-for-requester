package com.zhevakin.requester.sender.impl;


import com.zhevakin.requester.model.Answer;
import com.zhevakin.requester.sender.SendRequester;
import com.zhevakin.requester.sender.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class SenderImpl implements Sender {

    private final Map<HttpMethod, SendRequester> mapSenderRequester;

    @Autowired
    public SenderImpl(@Qualifier("senderMap") Map<HttpMethod, SendRequester> mapSenderRequester) {
        this.mapSenderRequester = mapSenderRequester;
    }

    public Answer send(String url, Map<String, String> headers, Map<String, String> params,
                       HttpMethod httpMethod, String body) {

        Answer answer = new Answer();

        try {

            answer = mapSenderRequester.get(httpMethod).execute(url, params, headers, body);
        } catch (IOException ignored) {
            return answer;
        }

       return answer;


    }
}
