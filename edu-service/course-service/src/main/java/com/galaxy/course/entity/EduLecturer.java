package com.galaxy.course.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 讲师
 * </p>
 *
 * @author 玩你个球儿
 * @since
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EduLecturer对象", description="讲师")
public class EduLecturer implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "讲师ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "讲师姓名")
    private String name;

    @ApiModelProperty(value = "讲师介绍,说明讲师")
    private String intro;

    @ApiModelProperty(value = "讲师经历,学历,经验")
    private String career;

    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师 3 明星讲师")
    private Integer level;

    @ApiModelProperty(value = "讲师头像")
    private String head;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic         //逻辑删除注解
    private Boolean did;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime eduCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime eduModified;

   @ApiModelProperty(value = "乐观锁版本")
    @Version
    private Integer version;


}
