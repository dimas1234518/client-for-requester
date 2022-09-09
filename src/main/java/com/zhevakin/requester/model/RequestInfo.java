package com.zhevakin.requester.model;

import com.zhevakin.requester.enums.TextMode;
import com.zhevakin.requester.enums.TypeAuthorization;
import com.zhevakin.requester.enums.TypeRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class RequestInfo {

    private final String id;
    private HttpMethod requestMethod;
    private String name;
    private TypeRequest typeRequest;
    private String request;
    private String parent;
    private Map<String, String> headers;
    //private final String fullRequest;
    private TypeAuthorization typeAuthorization;
    private String authToken;
    private String body;
    private TextMode typeBody;
    private TextMode typeResponseBody;
    private Date updateDate;

    public RequestInfo() {
        this.id = UUID.randomUUID().toString();
    }

    public RequestInfo(String id){
        this.id = id;
    }

    public boolean updateParent (RequestInfo requestInfo) {
        if (this.typeRequest == TypeRequest.COLLECTIONS)
            return false;
        else if (this.typeRequest == TypeRequest.FOLDER && requestInfo.typeRequest == TypeRequest.REQUEST)
            return false;
        else if (this.typeRequest == TypeRequest.REQUEST && requestInfo.typeRequest == TypeRequest.REQUEST)
            return false;
        this.parent = requestInfo.id;
        return true;
    }

    public boolean updateParent(String parent) {
        this.parent = parent;
        return true;
    }

    @Override
    public String toString() { return name;}


}
