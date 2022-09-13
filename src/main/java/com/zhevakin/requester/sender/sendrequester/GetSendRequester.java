package com.zhevakin.requester.sender.sendrequester;

import com.zhevakin.requester.model.Answer;
import com.zhevakin.requester.sender.SendRequester;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.zhevakin.requester.sender.sendrequester.SendRequesterUtils.makeUrl;
import static com.zhevakin.requester.sender.sendrequester.SendRequesterUtils.parseAnswer;


@Component
public class GetSendRequester implements SendRequester {
    @Override
    public Answer execute(String url, Map<String, String> params, Map<String, String> headers, String body) throws IOException {

        Answer answer = new Answer();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(makeUrl(url, params));
            for (Map.Entry<String, String> header : headers.entrySet())
                httpGet.setHeader(header.getKey(), header.getValue());
            parseAnswer(answer, httpClient.execute(httpGet));
            httpClient.close();
        } catch (IOException exception) {
            httpClient.close();
        }
        return answer;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }
}
