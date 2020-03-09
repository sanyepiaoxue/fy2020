package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;

public interface IShippingService {

    //添加收货地址
    ServerResponse add(Shipping shipping);

    //删除收货地址
    ServerResponse del(Integer shippingId);

    //修改收货地址
    ServerResponse update(Shipping shipping);

    //查询一个地址
    Shipping query(Integer shippingId);

    //查询所有
    ServerResponse list(Integer pageNum,Integer pageSize);
}
