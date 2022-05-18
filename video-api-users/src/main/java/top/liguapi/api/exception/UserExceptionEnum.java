package top.liguapi.api.exception;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/16 17:40
 */
public enum UserExceptionEnum {
    VERIFICATION_CODE_EXPIRED("400", "验证码已过期"),
    VERIFICATION_CODE_ERROR("400", "验证码错误"),
    TOKEN_IS_EMPTY("400", "令牌为空"),
    TOKEN_IS_ILLEGAL("400", "令牌不合法"),
    FILE_EXCEEDS_MAXIMUM("400", "文件不能超过给定的最大值");


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

    UserExceptionEnum(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
