package com.galaxy.common.exception;

import com.galaxy.common.enums.ResultEnum;
import lombok.Data;

@Data
public class MyException extends RuntimeException {

    private Integer code;
    private String message;


    public MyException(){}
    public MyException(ResultEnum resultEnum) {
         this.message = resultEnum.getMessage();
        this.code = resultEnum.getCode();
    }
    public MyException(Integer code,String message){
        this.code = code;
        this.message = message;

    }


}
