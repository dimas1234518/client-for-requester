package com.zhevakin.requester.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentUser {

    private String login;
    private String password;

    public CurrentUser(String login) {
        this.login = login;
    }


    public void setDomain(String domain) {
        StringBuilder stringBuilder = new StringBuilder();
        if (domain != null) {
            stringBuilder.append(domain);
            stringBuilder.append('\\');
        }
        stringBuilder.append(login);
        login = stringBuilder.toString();
    }

    @Override
    public String toString() {
        return login;
    }

    public String getCurrentUser() {
        return login;
    }
}
