package com.zhevakin.requester.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Answer {

    private Map<String, List<String>> headers;
    private HttpStatus httpStatus;
    private String body;

}
