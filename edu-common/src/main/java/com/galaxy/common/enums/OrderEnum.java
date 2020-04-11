package com.galaxy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderEnum {

    ORDER_DRAFT(0,"订单待付款"),
    ORDER_FINISH(1,"订单已完成"),
    ORDER_CLOSE(2,"订单已关闭"),
    ORDER_SOURCE_web(0,"App"),
    ORDER_source_app(1,"Web"),
    PAY_TYPE_ZFB(1,"支付宝支付"),
    PAY_TYPE_WX(2,"微信支付")


;


    public static  String getName(OrderEnum orderEnum){


        return orderEnum.getMsg();
    }

    private int code;
    private String msg;



}
