package top.liguapi.api.exception;

import lombok.Data;

/**
 * @Description controller异常
 * @Author lww
 * @Version 1.0
 * @Date 2022/5/16 17:00
 */
@Data
public class ControllerException extends RuntimeException {
    private String status;

    public ControllerException(String status, String message) {
        super(message);
        this.status = status;
    }
}
