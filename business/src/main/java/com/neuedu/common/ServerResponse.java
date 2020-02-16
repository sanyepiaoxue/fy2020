package com.neuedu.common;

//接口往前端响应对象

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ServerResponse<T> {
    private int status;
    private String msg;//接口返回的状态码 0:代表调用接口成功 非0：调用接口失败。失败信息封装到msg中。
    private T data;//当接口调用失败时，封装错误信息

    public ServerResponse() {
    }

    public ServerResponse(int status) {
        this.status = status;
    }

    public ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }


    /**
     * 当接口调用成功
     */
    public static ServerResponse serverResponseBySucess(){

        return new ServerResponse(0);
    }

    public static <T> ServerResponse serverResponseBySucess(String msg,T data){

        return new ServerResponse(0,msg,data);
    }



    /**
     * 当接口调用失败
     */

    public static ServerResponse serverResponseByFail(int status,String msg){

        return new ServerResponse(status,msg);
    }

    /**
     * 判断接口是否调用成功
     */
    public boolean isSucess(){
        return this.status == 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
