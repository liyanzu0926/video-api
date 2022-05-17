package top.liguapi.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author lww
 * @Version 1.0
 * @Date 2022/5/13 17:52
 */
public class JSONUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String writeValueAsString(Object o) {
        String s = null;
        try {
            s = objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }
}
