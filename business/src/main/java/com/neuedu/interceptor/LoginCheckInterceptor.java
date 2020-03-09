package com.neuedu.interceptor;

import com.google.gson.Gson;
import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {


    /**
     * 请求到达Controller之前先通过preHandle
     *  true：代表可以通过本拦截器，到达目标Controller
     *  false：拦截请求，返回到前端
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        //判断用户是否登录
        User user =(User)session.getAttribute(Consts.USER);
        if (user != null){//用户已经登录
            return true;//可以通过拦截器
        }

        //重写Response
        PrintWriter printWriter=null;
        try {
            response.reset();//重置
            response.addHeader("Content-Type","application/json;charset=utf-8");
            printWriter = response.getWriter();//获得字符打印流
            ServerResponse serverResponse = ServerResponse.serverResponseByFail(StatusEnum.NO_LOGIN.getStatus(),StatusEnum.NO_LOGIN.getDesc());
            Gson gson = new Gson();
            String json = gson.toJson(serverResponse);
            printWriter.write(json);//向前端打印
            printWriter.flush();//将缓冲区的数据强制输出，用于清空缓冲区，若直接调用close()方法，则可能会丢失缓冲区的数据。所以通俗来讲它起到的是刷新的作用
            printWriter.close();//关闭流
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(printWriter!=null){
                printWriter.close();
            }
        }

        return false;
    }

    /**
     * 当Controller往前端响应时，通过该拦截器postHandle方法
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 一次http完成后，调用afterCompletion方法
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
