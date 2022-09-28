package com.zhevakin.requester.sender.sendrequester;

import com.zhevakin.requester.model.Answer;
import com.zhevakin.requester.sender.SendRequester;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.zhevakin.requester.sender.sendrequester.SendRequesterUtils.makeUrl;
import static com.zhevakin.requester.sender.sendrequester.SendRequesterUtils.parseAnswer;

@Component
public class PostSendRequester implements SendRequester {
    @Override
    public Answer execute(String url, Map<String, String> params, Map<String, String> headers, String body) {
        Answer answer = new Answer();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(makeUrl(url, params));
            httpPost.setEntity(new StringEntity(body));
            for (Map.Entry<String, String> header : headers.entrySet())
                httpPost.setHeader(header.getKey(), header.getValue());
            parseAnswer(answer, httpClient.execute(httpPost));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

}
