package com.zhevakin.requester.additional;

import com.zhevakin.requester.enums.TextMode;
import com.zhevakin.requester.sender.SendRequester;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class PrettyTextConfig {

    private final Map<TextMode, PrettyText> myAutowireMap = new HashMap<>();

    public PrettyTextConfig(){}

    @Bean
    @Qualifier("prettyMap")
    public Map<TextMode, PrettyText> prettyMap(List<PrettyText> prettyTextList) {
        for (PrettyText prettyText : prettyTextList) {
            myAutowireMap.put(prettyText.getTextMode(), prettyText);
        }
        return myAutowireMap;
    }

}
