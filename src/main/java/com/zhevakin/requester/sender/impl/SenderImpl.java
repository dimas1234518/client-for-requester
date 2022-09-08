package com.zhevakin.requester.sender.impl;


import com.zhevakin.requester.model.Answer;
import com.zhevakin.requester.sender.Sender;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class SenderImpl implements Sender {

    public Answer send(String url, Map<String, String> headers, Map<String, String> params,
                       HttpMethod httpMethod, String body) {

        Answer answer = new Answer();
        try {
            final RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> entity = new HttpEntity<>(body, getHeaders(headers));
            ResponseEntity<String> response = restTemplate.exchange(makeUrl(url, params), httpMethod, entity, String.class);
            answer.setBody(response.getBody());
            answer.setHttpStatus(response.getStatusCode());
            answer.setHeaders(response.getHeaders());
        } catch (RestClientException exception) {

            answer.setBody(exception.getMessage());
            answer.setHttpStatus(HttpStatus.FORBIDDEN);
            answer.setHeaders(new HashMap<>());

        }

        return answer;


    }

    private HttpHeaders getHeaders(Map<String,String> headers) {

        HttpHeaders httpHeaders = new HttpHeaders();

        for (Map.Entry<String, String> header : headers.entrySet()) {
            httpHeaders.put(header.getKey(), Collections.singletonList(header.getValue()));
        }

        return httpHeaders;

    }

    private String makeUrl(String url, Map<String, String> params) {

        StringBuilder stringBuilder = new StringBuilder(url);

        stringBuilder.append('?');

        for (Map.Entry<String, String> param : params.entrySet()) {
            stringBuilder.append(param.getKey());
            stringBuilder.append("=");
            stringBuilder.append(param.getValue());
            stringBuilder.append("&");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return stringBuilder.toString();

    }

}
