package com.neuedu.service.impl;

import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IShippingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShippingServiceImpl implements IShippingService {

    @Resource
    ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Shipping shipping) {
        int count = shippingMapper.insert(shipping);
        if (count <= 0){
            return ServerResponse.serverResponseByFail(StatusEnum.ADDRESS_ADD_FAIL.getStatus(),StatusEnum.ADDRESS_ADD_FAIL.getDesc());
        }

        return ServerResponse.serverResponseBySucess(null,shipping.getId());
    }







}
