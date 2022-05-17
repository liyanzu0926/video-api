package top.liguapi.api.exception;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/16 17:40
 */
public enum SMSExceptionEnum {
    FREQUENT_REQUESTS("400", "发送太频繁，请稍后再试");

    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    SMSExceptionEnum(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
