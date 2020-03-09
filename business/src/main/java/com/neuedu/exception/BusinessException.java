package com.neuedu.exception;

public class BusinessException extends RuntimeException{

    private String msg;//异常名
    private int status;//异常码

    public BusinessException(){
        super();
    }
    public BusinessException(int status,String msg){
        super(msg);//调用父类构造
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
