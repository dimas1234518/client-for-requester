package com.zhevakin.requester.additional.pretty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.zhevakin.requester.additional.PrettyText;
import com.zhevakin.requester.enums.TextMode;
import org.springframework.stereotype.Component;

@Component
public class JsonPrettyText implements PrettyText {

    @Override
    public String prettyPrint(String text) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(text);
        text = gson.toJson(jsonElement);
        return text;
    }

    @Override
    public TextMode getTextMode() {
        return TextMode.JSON;
    }
}
