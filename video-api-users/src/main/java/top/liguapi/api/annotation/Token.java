package top.liguapi.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 用于需要获取用户信息的方法上
 * @Author lww
 * @Date 2022/5/17 22:02
 */
@Retention(RetentionPolicy.RUNTIME) // 运行时有效
@Target(ElementType.METHOD) // 作用于方法上
public @interface Token {
}
