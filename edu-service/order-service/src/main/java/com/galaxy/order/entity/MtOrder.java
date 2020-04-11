package com.galaxy.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Order对象", description="")
public class MtOrder implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String zfbNo;
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "下单用户")
    private String username;

    @ApiModelProperty(value = "订单总额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "应付总额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "支付方式 1-支付宝 2-微信")
    private Integer payType;

    @ApiModelProperty(value = "订单来源 1-pc 2-web")
    private Integer sourceType;

    @ApiModelProperty(value = "订单状态【0->待付款；1->已完成；2->已关闭；3->无效订单】")
    private Integer status;

    @ApiModelProperty(value = "可获得的积分")
    private double points;

    @ApiModelProperty(value = "删除状态 1-删除 0-未删除")

    @TableLogic
    private Boolean did;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime eduCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime eduModified;


}
