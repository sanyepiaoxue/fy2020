package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface IOrderService {

    //创建订单
    ServerResponse createOrder(Integer userId,Integer shippingId);

    //取消订单
    ServerResponse cancelOrder(Long orderNo);

    //根据订单号查询订单信息
    ServerResponse findOrderByOrderNo(Long orderNo);

    //支付成功后，修改订单状态
    ServerResponse updateOrder(Long orderNo,String payTime,Integer orderStatus);

    //获取订单的商品信息
    ServerResponse getOrder();

    //获取订单list
    ServerResponse list(Integer pageNum,Integer pageSize);

    //订单详情
    ServerResponse detail(Long orderNo);
}
