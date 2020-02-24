package com.neuedu.config;

import com.google.gson.Gson;
import com.neuedu.common.RedisApi;
import com.neuedu.common.ServerResponse;
import com.neuedu.utils.MD5Utils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect//声明切面类
@Component
public class RedisAspect {

    @Autowired
    RedisApi redisApi;

    //1.定义切入点execution表达式
    @Pointcut("execution(public * com.neuedu.service.impl.CategoryServiceImpl.get*(..))")
    public void test(){

    }

    //2.环绕通知
    @Around("test()")
    public Object around(ProceedingJoinPoint joinPoint){
        StringBuffer stringBuffer = new StringBuffer();

        //类名
        String className = joinPoint.getSignature().getDeclaringType().getName();
        stringBuffer.append(className);
        String name = joinPoint.getSignature().getName();//方法名
        stringBuffer.append(name);
        Object[] args = joinPoint.getArgs();
        for (Object o:args){//参数值
            stringBuffer.append(o);
        }

        //缓存key
        String cacheKey = MD5Utils.getMD5Code(stringBuffer.toString());
        System.out.println(cacheKey);
        //从缓存中读取数据
        String cacheValue = redisApi.get(cacheKey);
        Gson gson = new Gson();
        if (cacheValue == null){//缓存中没有数据
            try {
                Object o = joinPoint.proceed();//执行目标方法，读取db
                String valueStr = gson.toJson(o);
                //写入缓存
                redisApi.set(cacheKey,valueStr);
                return  o;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }else {//缓存中有数据
            ServerResponse serverResponse = gson.fromJson(cacheValue,ServerResponse.class);
            return serverResponse;
        }
        return null;
    }

}
