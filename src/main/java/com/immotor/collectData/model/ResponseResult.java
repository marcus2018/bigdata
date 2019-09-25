package com.immotor.collectData.model;

public class ResponseResult {
    public static  final  Integer FAIL=-1;//失败
    public  static final Integer SUCCESS=0;//成功
    private Object data;
    private Integer code;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
    public  ResponseResult(Object data){
        this.data=data;
    }

    public static ResponseResult Result(Object data){
        ResponseResult responseResult=new ResponseResult(data);
        responseResult.setCode(SUCCESS);
        return  responseResult;
    }
    public static ResponseResult ResultError(Object data){
        ResponseResult responseResult=new ResponseResult(data);
        responseResult.setCode(FAIL);
        return  responseResult;
    }
}
