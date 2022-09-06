package com.zhevakin.requester.model.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

    private final String type;
    private final String accessToken;
    private final String refreshToken;

    public JwtResponse(String type, String accessToken, String refreshToken) {
        this.type = type;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
