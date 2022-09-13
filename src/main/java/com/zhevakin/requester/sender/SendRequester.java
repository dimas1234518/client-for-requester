package com.zhevakin.requester.sender;

import com.zhevakin.requester.model.Answer;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.Map;

public interface SendRequester {

    Answer execute(String url, Map<String, String> params, Map<String, String> headers, String body) throws IOException;

    HttpMethod getMethod();
}
