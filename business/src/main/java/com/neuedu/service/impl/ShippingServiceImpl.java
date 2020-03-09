package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IShippingService;
import com.neuedu.utils.DateUtils;
import com.neuedu.vo.ShippingVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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


    @Override
    public ServerResponse del(Integer shippingId) {
        if (shippingId == null){
            return ServerResponse.serverResponseByFail(StatusEnum.ADDRESS_DEL_FAIL.getStatus(),StatusEnum.ADDRESS_DEL_FAIL.getDesc());
        }
        shippingMapper.deleteByPrimaryKey(shippingId);
        return ServerResponse.serverResponseBySucess(null,"删除地址成功");
    }

    @Override
    public ServerResponse update(Shipping shipping) {

        if (shipping.getId() == null){
            return ServerResponse.serverResponseByFail(StatusEnum.ADDRESS_UPDATE_FAIL.getStatus(),StatusEnum.ADDRESS_UPDATE_FAIL.getDesc());
        }
        if (shipping.getUserId() == null){
            return ServerResponse.serverResponseByFail(StatusEnum.ADDRESS_UPDATE_FAIL.getStatus(),StatusEnum.ADDRESS_UPDATE_FAIL.getDesc());
        }
        shippingMapper.updateByPrimaryKey(shipping);

        return ServerResponse.serverResponseBySucess(null,"地址更新成功");
    }

    @Override
    public Shipping query(Integer shippingId) {

        return shippingMapper.selectByPrimaryKey(shippingId);
    }

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectAll();
        List<ShippingVO> shippingVOList = new ArrayList<>();
        for (Shipping shipping:shippingList){
            ShippingVO shippingVO = new ShippingVO();

            shippingVO.setId(shipping.getId());
            shippingVO.setUserId(shipping.getUserId());
            shippingVO.setReceiverName(shipping.getReceiverName());
            shippingVO.setReceiverPhone(shipping.getReceiverPhone());
            shippingVO.setReceiverMobile(shipping.getReceiverMobile());
            shippingVO.setReceiverProvince(shipping.getReceiverProvince());
            shippingVO.setReceiverCity(shipping.getReceiverCity());
            shippingVO.setReceiverAddress(shipping.getReceiverAddress());
            shippingVO.setReceiverZip(shipping.getReceiverZip());
            shippingVO.setCreateTime(DateUtils.date2Str(shipping.getCreateTime()));
            shippingVO.setUpdateTime(DateUtils.date2Str(shipping.getUpdateTime()));

            shippingVOList.add(shippingVO);
        }

        //构建分页模型
        PageInfo pageInfo = new PageInfo(shippingVOList);

        return ServerResponse.serverResponseBySucess(null,pageInfo);
    }
}
