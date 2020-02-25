package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;

public interface IShippingService {

    //添加收货地址
    ServerResponse add(Shipping shipping);
}
