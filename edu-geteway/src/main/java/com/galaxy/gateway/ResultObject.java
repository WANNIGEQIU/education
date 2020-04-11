package com.galaxy.gateway;

import com.galaxy.common.enums.ResultEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultObject {

    private int code;
    private String message;

    public ResultObject(){}
    public ResultObject(ResultEnum resultEnum){
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }
}
