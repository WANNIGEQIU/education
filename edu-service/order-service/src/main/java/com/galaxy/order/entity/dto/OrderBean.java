package com.galaxy.order.entity.dto;


import com.galaxy.common.bean.CourseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderBean extends CourseVo {

    @ApiModelProperty(value = "id")
    private String id;

    private String orderNo;

    private String courseName;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "下单用户")
    private String userName;

    @ApiModelProperty(value = "订单总额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "应付总额")
    private BigDecimal payAmount;

    private String courseId;

    @ApiModelProperty(value = "支付方式 1-支付宝 2-微信")
    private Integer payType;

    @ApiModelProperty(value = "订单来源 1-pc 2-app")
    private Integer sourceType;

    @ApiModelProperty(value = "订单状态【0->待付款；1->已完成；2->已关闭；3->无效订单】")
    private Integer orderStatus;

    @ApiModelProperty(value = "可获得的积分")
    private String points;

    @ApiModelProperty(value = "删除状态 1-删除 0-未删除")
    private Integer did;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    private String startTime;

    private String endTime;






}
