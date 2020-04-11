package com.galaxy.statistics.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EduStatistics对象", description="")
public class EduStatistics implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "统计日期")
    private String recordDate;

    @ApiModelProperty(value = "注册人数")
    private Integer registerNum;

    @ApiModelProperty(value = "订单量")
    private Integer orderNum;

    @ApiModelProperty(value = "购买量")
    private Long buyNum;

    @ApiModelProperty(value = "每日新增课程数")
    private Integer courseNum;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime eduCreate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime eduModified;


}
