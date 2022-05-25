package top.liguapi.api.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.liguapi.api.annotation.Token;
import top.liguapi.api.constant.RedisPrefix;
import top.liguapi.api.entity.pojo.User;
import top.liguapi.api.exception.UserException;
import top.liguapi.api.exception.UserExceptionEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 用于从token中获取用户信息
 * @Author lww
 * @Date 2022/5/17 22:07
 */

/**
 * 自定义拦截器，拦截token，从而获取用户信息，将用户信息添加到上下文中。
 * 自定义Token注解，为所有需要用户信息的方法加上注解，就可以从上下文中
 * 获取用户信息
 * 1.自定义一个注解
 * 2.自定义一个拦截器
 * 3.实现WebMvcConfigurer接口，并实现其中addInterceptors方法，将自定义的拦截器添加进去
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private RedisTemplate redisTemplate;

    @Autowired
    public TokenInterceptor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            boolean annotationPresent = ((HandlerMethod) handler).getMethod().isAnnotationPresent(Token.class);
            if (annotationPresent) {
                String token = request.getParameter("token");
                if (token == null) {
                    throw new UserException(UserExceptionEnum.TOKEN_IS_EMPTY);
                }
                User user = (User) redisTemplate.opsForValue().get(RedisPrefix.TOKEN_KEY + token);
                if (user == null) {
                    throw new UserException(UserExceptionEnum.TOKEN_IS_ILLEGAL);
                }
                request.setAttribute("token", token);
                request.setAttribute("user", user);
            }
            return true;
        }else {
            return false;
        }
    }
}
