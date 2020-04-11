package com.galaxy.order.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDto {

    private String id;

    @ApiModelProperty(value = "数量")
    private Integer totalNum;

    private String zfbNo;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "支付类型 0 线上 1 其他")
    private Integer payType;

    @ApiModelProperty(value = "下单用户")
    private String username;

    @ApiModelProperty(value = "订单状态 0 未完成 1 已完成")
    private Integer orderStatus;

    @ApiModelProperty(value = "支付状态 0 未支付 1 已支付")
    private Integer payStatus;

    @ApiModelProperty(value = "订单来源 0 web 1 app 2 微信小程序 3")
    private Integer sourceType;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "实付金额")
    private BigDecimal payMoney;

    @ApiModelProperty(value = "课程名字")
    private String courseName;


    @ApiModelProperty(value = "优惠金额")
    @TableField("Preferential_amount")
    private BigDecimal preferentialAmount;


    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "查询条件开始时间")
    private String beginTime;
    @ApiModelProperty(value = "查询条件结束时间")
    private String endTime;
}
