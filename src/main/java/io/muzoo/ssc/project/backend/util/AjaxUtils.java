package io.muzoo.ssc.project.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

public class AjaxUtils {

    public static String convertToString(Object objectvalue) {
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(writer, objectvalue);
            return writer.toString();
        } catch (IOException e) {
            return "[bad object/conversion]";
        }
    }
}
