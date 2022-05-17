package top.liguapi.api.exception;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/16 17:37
 */
public class SMSException extends ControllerException {
    public SMSException(SMSExceptionEnum smsExceptionEnum) {
        super(smsExceptionEnum.getStatus(), smsExceptionEnum.getMessage());
    }
}
