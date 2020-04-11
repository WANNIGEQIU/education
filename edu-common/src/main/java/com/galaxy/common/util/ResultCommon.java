package com.galaxy.common.util;


import com.galaxy.common.enums.ResultEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResultCommon<T> implements Serializable {
    private static final long serialVersionUID = 100L;

    private boolean flag;    // 是否成功
    private Integer code;    //状态码
    private String message; //返回消息
    private T data;         //返回数据


    public ResultCommon(){};

    // 成功
    public static <T> ResultCommon resultOk(T data){
        ResultCommon<T> r = new ResultCommon<>();
        r.setFlag(true);
        r.setCode(ResultEnum.SUCCESS.getCode());
        r.setMessage(ResultEnum.SUCCESS.getMessage());
        r.setData(data);
        return r;
    }
    public static <T> ResultCommon resultOk(){
        ResultCommon<T> r = new ResultCommon<>();
        r.setFlag(true);
        r.setMessage(ResultEnum.SUCCESS.getMessage());
        r.setCode(ResultEnum.SUCCESS.getCode());
        return  r;
    }
    //失败的
    public static <T> ResultCommon  resultFail(){
        ResultCommon r = new ResultCommon();
        r.setFlag(false);
        r.setCode(ResultEnum.ERROR.getCode());
        r.setMessage(ResultEnum.ERROR.getMessage());
        return  r;
    }

    // 异常
    public static <T> ResultCommon exception(){
        ResultCommon r = new ResultCommon();
        r.setFlag(false);
        return r;
    }

    public ResultCommon flag(Boolean flag){
        this.setFlag(flag);
        return this;
    }
    public ResultCommon code(int code){
        this.setCode(code);
        return this;
    }
    public ResultCommon msg(String msg){
        this.setMessage(msg);
        return this;
    }
    public ResultCommon codeAndMsg(ResultEnum resultEnum){
        this.setCode(resultEnum.getCode());
        this.setMessage(resultEnum.getMessage());
        return this;
    }
    public ResultCommon codeAndMsg(Integer code ,String message){
        this.setCode(code);
        this.setMessage(message);
        return this;
    }
    public ResultCommon data(T data){
        this.setData(data);
        return this;
    }







}
