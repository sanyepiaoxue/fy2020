package com.neuedu;

import com.neuedu.interceptor.AutoIdempontentInterceptor;
import com.neuedu.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@SpringBootConfiguration
public class SpringBootInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    LoginCheckInterceptor loginCheckInterceptor;
    @Autowired
    AutoIdempontentInterceptor autoIdempontentInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(autoIdempontentInterceptor);

        List<String> loginPath = new ArrayList<>();
        loginPath.add("/manage/**");
        loginPath.add("/user/**");
        loginPath.add("/cart/**");
        loginPath.add("/order/**");
        loginPath.add("/shipping/**");

        List<String> excludePath = new ArrayList<>();
        excludePath.add("/user/login.do");
        excludePath.add("/user/register.do");
        excludePath.add("/user/forget_get_question.do");
        excludePath.add("/manage/user/login.do");
        excludePath.add("/order/alipay_callback.do");

        registry.addInterceptor(loginCheckInterceptor).
                addPathPatterns(loginPath).//添加需要拦截的路径
                excludePathPatterns(excludePath);//排除不需要拦截的路径
    }
}
