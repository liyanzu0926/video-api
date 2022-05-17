package top.liguapi.api.exception;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/16 17:37
 */
public class UserException extends ControllerException {
    public UserException(UserExceptionEnum userExceptionEnum) {
        super(userExceptionEnum.getStatus(), userExceptionEnum.getMessage());
    }
}
