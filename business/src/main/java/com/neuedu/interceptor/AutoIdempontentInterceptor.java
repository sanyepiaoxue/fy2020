package com.neuedu.interceptor;

import com.google.gson.Gson;
import com.neuedu.annotation.AutoIdempontent;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Component
public class AutoIdempontentInterceptor implements HandlerInterceptor {

    @Autowired
    ITokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {


        HandlerMethod handlerMethod= (HandlerMethod) handler;
        Method method=handlerMethod.getMethod();
        AutoIdempontent autoIdempontent= method.getAnnotation(AutoIdempontent.class);
        if(autoIdempontent!=null){
            //该方法上含有AutoIdempontent注解

            //校验token
            ServerResponse serverResponse=tokenService.checkToken(request);

            if(!serverResponse.isSucess()){
                //重写Response
                PrintWriter printWriter=null;
                try {
                    response.reset();//重置
                    response.addHeader("Content-Type","application/json;charset=utf-8");
                    printWriter= response.getWriter();
                    Gson gson=new Gson();
                    String json=gson.toJson(serverResponse);
                    printWriter.write(json);
                    printWriter.flush();
                    printWriter.close();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(printWriter!=null){
                        printWriter.close();
                    }
                }
            }else{
                return true;
            }

        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}

