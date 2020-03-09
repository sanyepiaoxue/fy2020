package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import java.util.Map;

public interface IPayService {
    ServerResponse pay(Long orderNo);

    String callbackLogic(Map<String,String> signParam);

    /*//查询订单状态
    ServerResponse queryPay(Long orderNo);*/
}
