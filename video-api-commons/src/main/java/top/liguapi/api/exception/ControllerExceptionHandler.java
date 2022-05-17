package top.liguapi.api.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description controller异常处理增强
 * @Author lww
 * @Version 1.0
 * @Date 2022/5/16 16:44
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = ControllerException.class)
    @ResponseBody
    public Map<String, String> handler(ControllerException e) {
        Map<String, String> map = new HashMap<>();
        map.put("status", e.getStatus());
        map.put("message", e.getMessage());
        return map;
    }
}
