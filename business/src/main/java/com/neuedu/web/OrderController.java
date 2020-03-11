package com.neuedu.web;

import com.neuedu.annotation.AutoIdempontent;
import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import com.neuedu.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/order/")
public class OrderController {
    @Resource
    IOrderService orderService;

    @AutoIdempontent
    @RequestMapping("create.do")
    public ServerResponse create(Integer shippingId, HttpSession session){
        //step1:先判断用户是否登录
        User user=(User)session.getAttribute(Consts.USER);


        return orderService.createOrder(user.getId(),shippingId);
    }

    @RequestMapping("cancel.do")
    public ServerResponse cancel(Long orderNo){
        return orderService.cancelOrder(orderNo);
    }

    @RequestMapping("get_order_cart_product.do")
    public ServerResponse get_order(HttpSession session){
        User user=(User)session.getAttribute(Consts.USER);
        return orderService.getOrder();
    }
}
