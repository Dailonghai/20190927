package com.lanxin.util;

/**
 * Created by aptx4869 on 2019/8/19.
 */
public class Result {

    private Integer code;
    private String msg;
    private Object data;

    public static Result ok(){

        Result result=new Result();
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    public static Result ok(Object obj){

        Result result=new Result();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(obj);
        return result;
    }

    public static Result faild(){

        Result result=new Result();
        result.setCode(500);
        result.setMsg("系统内部异常");
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
