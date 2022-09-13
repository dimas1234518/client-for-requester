package com.zhevakin.requester.sender;

import com.zhevakin.requester.model.Answer;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.Map;

public interface Sender {

    Answer send(String url, Map<String, String> headers, Map<String, String> params,
                       HttpMethod httpMethod, String body);

}
