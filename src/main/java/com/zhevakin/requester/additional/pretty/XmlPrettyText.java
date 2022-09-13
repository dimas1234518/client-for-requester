package com.zhevakin.requester.additional.pretty;

import com.zhevakin.requester.additional.PrettyText;
import com.zhevakin.requester.enums.TextMode;
import org.springframework.stereotype.Component;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

@Component
public class XmlPrettyText implements PrettyText {

    @Override
    public String prettyPrint(String text) {
        try {
            Source xmlInput = new StreamSource(new StringReader(text));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 4);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            return text;
        }
    }

    @Override
    public TextMode getTextMode() {
        return TextMode.XML;
    }
}
