package com.galaxy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum  StatisticsEnum {

    USER_REGISTER("注册人数",1),
    ORDER_NUMS("订单量",2),
    COURSE_NUMS("课程增长量",3),
    COURSE_SELL("课程销量",4)

    ;




    private String name;
    private int type;
}
