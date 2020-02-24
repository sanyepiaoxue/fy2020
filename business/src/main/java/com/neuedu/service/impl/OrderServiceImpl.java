package com.neuedu.service.impl;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.OrderItemMapper;
import com.neuedu.dao.OrderMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Order;
import com.neuedu.pojo.OrderItem;
import com.neuedu.service.ICartService;
import com.neuedu.service.IOrderService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.BigDecimalUtil;
import com.neuedu.utils.DateUtils;
import com.neuedu.vo.OrderItemVO;
import com.neuedu.vo.OrderVO;
import com.neuedu.vo.ProductDetailVO;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    ICartService iCartService;
    @Autowired
    IProductService iProductService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;


    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {

        //1.参数的非空校验
        if (shippingId == null){
            return ServerResponse.serverResponseByFail(StatusEnum.ADDRESS_NOT_EMPYT.getStatus(),StatusEnum.ADDRESS_NOT_EMPYT.getDesc());
        }
        //2.根据userId查询购物车中已经选中的商品List<Cart>
        ServerResponse<List<Cart>> serverResponse = iCartService.findCartByUserIdAndChecked(userId);

        if (!serverResponse.isSucess()){
            return serverResponse;
        }

        List<Cart> cartList = serverResponse.getData();
        if (cartList==null || cartList.size()==0){
            return ServerResponse.serverResponseByFail(StatusEnum.USER_CART_EMPTY.getStatus(),StatusEnum.USER_CART_EMPTY.getDesc());
        }
        //3.List<Cart>转换成List<OrderItem>
        ServerResponse<List<OrderItem>> serverResponse1 = assembleOrderItemList(cartList,userId);
        if (!serverResponse1.isSucess()){
            return serverResponse1;
        }

        //4.生成订单，并插入订单库
        List<OrderItem> orderItemList = serverResponse1.getData();
        ServerResponse<Order> serverResponse2 = generateOrder(userId,orderItemList,shippingId);
        if (!serverResponse2.isSucess()){
            return serverResponse2;
        }
        //5.将订单明细批量插入订单明细库
        Order order = serverResponse2.getData();
        for (OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }

        int count = orderItemMapper.insertBatch(orderItemList);
        if (count <= 0){
            return ServerResponse.serverResponseByFail(StatusEnum.ORDER_ITEM_CREATE_FAIL.getStatus(),StatusEnum.ORDER_ITEM_CREATE_FAIL.getDesc());
        }
        //6.减商品库存
        reduceStock(orderItemList);
        //7.清空购物车中已经下单的商品
        ServerResponse serverResponse3 = cleanCart(cartList);
        if (!serverResponse3.isSucess()){
            return serverResponse3;
        }

        //8.前端返回OrderVO
        OrderVO orderVO = assembleOrderVo(order,orderItemList,shippingId);
        return ServerResponse.serverResponseBySucess(null,orderVO);
    }


    public OrderVO assembleOrderVo(Order order,List<OrderItem> orderItemList,Integer shippingId){
        OrderVO orderVO = new OrderVO();

        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        orderVO.setPostage(order.getPostage());
        orderVO.setStatus(order.getStatus());
        orderVO.setPaymentTime(DateUtils.date2Str(order.getPaymentTime()));
        orderVO.setSendTime(DateUtils.date2Str(order.getSendTime()));
        orderVO.setEndTime(DateUtils.date2Str(order.getEndTime()));
        orderVO.setCreateTime(DateUtils.date2Str(order.getCreateTime()));
        orderVO.setCloseTime(DateUtils.date2Str(order.getCloseTime()));
        orderVO.setShippingId(shippingId);

        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO = convertOrderItemVo(orderItem);
            orderItemVOList.add(orderItemVO);
        }

        orderVO.setOrderItemVOList(orderItemVOList);

        return orderVO;
    }


    //orderItem-->orderItemVO
    private OrderItemVO convertOrderItemVo(OrderItem orderItem){
        if (orderItem == null){
            return null;
        }
        OrderItemVO orderItemVO = new OrderItemVO();

        orderItemVO.setOrderNo(orderItem.getOrderNo());
        orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVO.setProductId(orderItem.getProductId());
        orderItemVO.setProductImage(orderItem.getProductImage());
        orderItemVO.setProductName(orderItem.getProductName());
        orderItemVO.setProductId(orderItem.getProductId());
        orderItemVO.setQuantity(orderItem.getQuantity());
        orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        orderItemVO.setCreateTime(DateUtils.date2Str(orderItem.getCreateTime()));
        return orderItemVO;
    }

    //清空购物车已下单商品
    private ServerResponse cleanCart(List<Cart> cartList){
        return iCartService.deleteBatchByIds(cartList);
    }

    //扣库存
    private ServerResponse reduceStock(List<OrderItem> orderItemList){
        for (OrderItem orderItem : orderItemList){
            Integer productId = orderItem.getProductId();
            Integer quantity = orderItem.getQuantity();
            //根据商品id扣库存
            ServerResponse serverResponse = iProductService.reduceStocke(productId,quantity);
            if (!serverResponse.isSucess()){
                return serverResponse;
            }
        }
        return ServerResponse.serverResponseBySucess();
    }

    private ServerResponse generateOrder(Integer userId,List<OrderItem> orderItems,Integer shippingId){
        Order order = new Order();
        order.setUserId(userId);
        order.setShippingId(shippingId);
        //订单总金额
        order.setPayment(getOrderTotalPrice(orderItems));
        order.setPaymentType(1);
        order.setPostage(0);
        order.setStatus(Consts.OrderStatusEnum.UNPAY.getStatus());
        order.setOrderNo(generateOrderNo());
        //将订单落库
        int count = orderMapper.insert(order);
        if (count <= 0){
            ServerResponse.serverResponseByFail(StatusEnum.ORDER_CREATE_FAIL.getStatus(),StatusEnum.ORDER_CREATE_FAIL.getDesc());
        }
        return ServerResponse.serverResponseBySucess(null,order);
    }

    //生成订单号
    private long generateOrderNo(){
        return System.currentTimeMillis();
    }

    //计算总金额
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItems){
        BigDecimal orderTotalPrice = new BigDecimal("0");
        for (OrderItem orderItem:orderItems){
            orderTotalPrice = BigDecimalUtil.add(String.valueOf(orderTotalPrice.doubleValue()),String.valueOf(orderItem.getCurrentUnitPrice()));
        }
        return orderTotalPrice;
    }

    private ServerResponse assembleOrderItemList(List<Cart> cartList,Integer userId){
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Cart cart : cartList){
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cart.getProductId());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setUserId(userId);

            //根据商品id查询商品信息
            ServerResponse<ProductDetailVO> serverResponse = iProductService.detail(cart.getProductId());
            if (!serverResponse.isSucess()){
                return serverResponse;
            }
            //商品是否处于在售状态
            ProductDetailVO productDetailVO = serverResponse.getData();
            if (productDetailVO.getStatus()!= Consts.ProductStatusEnum.PRODUCT_ONLINE.getStatus()){
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_NOT_EXISTS.getStatus(),StatusEnum.PRODUCT_NOT_EXISTS.getDesc());
            }
            //判断商品库存是否充足
            if (productDetailVO.getStock()<cart.getQuantity()){
                return ServerResponse.serverResponseByFail(StatusEnum.PRODUCT_STOCK_NOT_FULL.getStatus(),StatusEnum.PRODUCT_STOCK_NOT_FULL.getDesc());
            }
            orderItem.setProductId(cart.getProductId());
            orderItem.setCurrentUnitPrice(productDetailVO.getPrice());
            orderItem.setProductImage(productDetailVO.getMainImage());
            orderItem.setProductName(productDetailVO.getName());
            orderItem.setTotalPrice(BigDecimalUtil.multi(String.valueOf(productDetailVO.getPrice().doubleValue()),String.valueOf(cart.getQuantity())));
            orderItemList.add(orderItem);
        }
        return ServerResponse.serverResponseBySucess(null,orderItemList);
    }






}
