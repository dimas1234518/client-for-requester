package com.zhevakin.requester.additional;

import com.zhevakin.requester.enums.TextMode;

public interface PrettyText {

    String prettyPrint (String text);

    TextMode getTextMode();

}
