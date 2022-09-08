package com.zhevakin.requester.front.generator;

import com.zhevakin.requester.enums.TextMode;
import com.zhevakin.requester.enums.TypeRequest;
import com.zhevakin.requester.model.RequestInfo;
import org.springframework.http.HttpMethod;

public class RequestGenerator {

    public static RequestInfo createRequest() {

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setName("New Request");
        requestInfo.setTypeRequest(TypeRequest.REQUEST);
        requestInfo.setRequestMethod(HttpMethod.GET);
        requestInfo.setTypeResponseBody(TextMode.NONE);
        requestInfo.setTypeBody(TextMode.NONE);

        return requestInfo;

    }

    public static RequestInfo createCollection() {

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setName("New Collection");
        requestInfo.setTypeRequest(TypeRequest.COLLECTIONS);
        return requestInfo;

    }

    public static RequestInfo createFolder() {

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setName("New Folder");
        requestInfo.setTypeRequest(TypeRequest.FOLDER);
        return requestInfo;

    }
}
