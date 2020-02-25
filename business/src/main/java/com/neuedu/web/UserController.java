package com.neuedu.web;
import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    //注册
    @RequestMapping("/register.do")
    public ServerResponse register(User user){
        return userService.registerLogic(user);
    }

    //登录接口
    @RequestMapping("/login.do")
    public ServerResponse login(String username, String password, HttpSession session){
        ServerResponse response = userService.loginLogic(username,password);
        if (response.isSucess()){
            //登陆成功
            session.setAttribute(Consts.USER,response.getData());
        }
        return response;
    }

    //退出登录接口
    @RequestMapping("/logout.do")
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(Consts.USER);
        return ServerResponse.serverResponseBySucess();
    }
}
