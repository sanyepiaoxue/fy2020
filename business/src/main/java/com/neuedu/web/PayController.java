package com.neuedu.web;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.neuedu.annotation.AutoIdempontent;
import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.IPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/order/")
public class PayController {

    @Resource
    IPayService iPayService;
    @Value("${alipay.public.key}")
    String alipayPublicKey;

    @AutoIdempontent
    @RequestMapping("pay.do")
    public ServerResponse pay(Long orderNo){
        return iPayService.pay(orderNo);
    }

    //支付宝回调接口
    //当前端用户支付完成后，支付宝服务器会调用该接口
    @RequestMapping("alipay_callback.do")
    public String alipay_callback(HttpServletRequest request){
        Map<String,String[]> callbackParam = request.getParameterMap();
        Iterator<Map.Entry<String,String[]>> iterator = callbackParam.entrySet().iterator();
        Map<String,String> signMap = new HashMap<>();
        while (iterator.hasNext()){
            Map.Entry<String,String[]> entry = iterator.next();
            String key = entry.getKey();
            String[] values = entry.getValue();
            StringBuffer buffer = new StringBuffer();
            for (int i=0;i<values.length;i++){
                buffer.append(values[i]+",");
            }
            String value = buffer.toString();
            value = value.substring(0,value.length()-1);
            signMap.put(key,value);

        }

        //验签
        signMap.remove("sign_type");
        try {
            boolean result = AlipaySignature.rsaCheckV2(signMap,alipayPublicKey,"utf-8","RSA2");
            if (!result){
                return "fail";
            }
        }catch (AlipayApiException e){
            e.printStackTrace();
        }
        //2.处理业务逻辑
        return iPayService.callbackLogic(signMap);
    }

    @RequestMapping("query_order_pay_status.do")
    public ServerResponse query(HttpSession session,Long orderNo){
        User user=(User)session.getAttribute(Consts.USER);
        return iPayService.queryState(orderNo);
    }
}
