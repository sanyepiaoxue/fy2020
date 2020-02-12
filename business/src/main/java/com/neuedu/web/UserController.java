package com.neuedu.web;


import com.neuedu.dao.UserMapper;
import com.neuedu.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
//测试类（逆向工程初期测试使用）
@Controller
public class UserController {


    @Resource
    UserMapper userMapper;

    @RequestMapping("/find")
    @ResponseBody
    public User find(){
        User user = userMapper.selectByPrimaryKey(1);

        return user;
    }
}
