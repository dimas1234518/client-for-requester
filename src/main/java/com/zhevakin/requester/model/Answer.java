package com.zhevakin.requester.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Answer {

    private Map<String, List<String>> headers;
    private HttpStatus httpStatus;
    private String body;

    public Map<String, String> getSimpleHeaders() {
        Map<String, String> returnHeaders = new HashMap<>();

        for (Map.Entry<String, List<String>> heads : headers.entrySet()) {
            returnHeaders.put(heads.getKey(), heads.getValue().get(0));
        }

        return returnHeaders;

    }

}
