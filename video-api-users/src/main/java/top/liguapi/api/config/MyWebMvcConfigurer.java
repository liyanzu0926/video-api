package top.liguapi.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.liguapi.api.interceptor.TokenInterceptor;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/17 22:22
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    private TokenInterceptor tokenInterceptor;

    @Autowired
    public MyWebMvcConfigurer(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**"); // 拦截所有
    }
}
