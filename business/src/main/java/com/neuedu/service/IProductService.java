package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;

public interface IProductService {

    ServerResponse addorUpdate(Product product);

    //前台商品检索
    ServerResponse list(Integer categoryId,String keyword,Integer pageNum,Integer pageSize,String orderBy);

}
