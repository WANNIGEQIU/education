package com.galaxy.order.entity.vo;

import com.galaxy.order.entity.OrderItem;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderVo {

    private String orderNo;

    private String zfbNo;
    private String userName;

    private String orderPrice;

    private String payPrice;

    private Integer payType;
    private String payTypeStr;

    private Integer sourceType;
    private String sourceStr;

    private Integer orderStatus;
    private String orderStatusStr;

    private String eduCreate;

    private String payTime;

    private String courseName;

    private Integer skuNum = 1;

    private String lecturerName;

    private OrderItem orderItem;




}
