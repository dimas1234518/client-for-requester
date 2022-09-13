package com.zhevakin.requester.additional.pretty;

import com.zhevakin.requester.additional.PrettyText;
import com.zhevakin.requester.enums.TextMode;
import org.springframework.stereotype.Component;

@Component
public class TextPrettyText implements PrettyText {
    @Override
    public String prettyPrint(String text) {
        return text;
    }

    @Override
    public TextMode getTextMode() {
        return TextMode.TEXT;
    }
}
