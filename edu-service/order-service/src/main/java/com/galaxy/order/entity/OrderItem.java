package com.galaxy.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

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
@ApiModel(value="OrderItem对象", description="")
public class OrderItem implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "二级分类id")
    private String categoryId;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "购买数量")
    private Integer skuNum;

    @ApiModelProperty(value = "课程图片")
    private String courseImg;

    @ApiModelProperty(value = "课程价格")
    private BigDecimal coursePrice;

    @ApiModelProperty(value = "赠送的积分值")
    private double gift;

    @ApiModelProperty(value = "讲师")
    private String lecturerId;
    @ApiModelProperty(value = "讲师name")
    private String lecturerName;

    private String categoryPid;


}
