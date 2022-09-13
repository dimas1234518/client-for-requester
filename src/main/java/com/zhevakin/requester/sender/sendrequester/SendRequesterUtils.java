package com.zhevakin.requester.sender.sendrequester;

import com.zhevakin.requester.model.Answer;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendRequesterUtils {

    public static void parseAnswer(Answer answer, CloseableHttpResponse execute) throws IOException {
        answer.setBody(EntityUtils.toString(execute.getEntity()));
        answer.setHttpStatus(HttpStatus.valueOf(execute.getStatusLine().getStatusCode()));
        Map<String, List<String>> responseHeaders = new HashMap<>();
        for (Header header : execute.getAllHeaders())
            responseHeaders.put(header.getName(), Collections.singletonList(header.getValue()));
        answer.setHeaders(responseHeaders);
    }

    public static String makeUrl(String url, Map<String, String> params) {

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
