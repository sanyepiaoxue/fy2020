package com.neuedu.web;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;
import com.neuedu.pojo.User;
import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    IShippingService iShippingService;

    @RequestMapping("add.do")
    public ServerResponse add(Shipping shipping, HttpSession session){
        User user = (User)session.getAttribute(Consts.USER);
        shipping.setUserId(user.getId());
        return iShippingService.add(shipping);
    }

    @RequestMapping("del.do")
    public ServerResponse del(Integer shippingId,HttpSession session){
        User user = (User)session.getAttribute(Consts.USER);
        return iShippingService.del(shippingId);
    }

    @RequestMapping("update.do")
    public ServerResponse update(Shipping shipping,HttpSession session){
        User user = (User)session.getAttribute(Consts.USER);
        return iShippingService.update(shipping);
    }

    @RequestMapping("select.do")
    public Shipping select(Integer shippingId,HttpSession session){
        User user = (User)session.getAttribute(Consts.USER);
        return iShippingService.query(shippingId);
    }

    @RequestMapping("list.do")
    public ServerResponse list(@RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10") Integer   pageSize,
                               HttpSession session){
        User user = (User)session.getAttribute(Consts.USER);
        return iShippingService.list(pageNum,pageSize);
    }
}
