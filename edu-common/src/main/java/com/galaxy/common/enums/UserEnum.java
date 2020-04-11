package com.galaxy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserEnum {

    USER_IS_USERD(0,"用户未禁用"),
    USER_UNUSERD(1,"用户已禁用")




    ;





    private int code;
    private String message;
}
