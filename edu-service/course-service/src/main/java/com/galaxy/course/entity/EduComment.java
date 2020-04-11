package com.galaxy.course.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EduComment", description="课程评论")
public class EduComment implements Serializable {
    private static final long serialVersionUID=1232L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String memberId;
    private String courseId;
    private String teacherId;
    private String username;
    private String avatar;
    private String content;
    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic         //逻辑删除注解
    private Boolean did;
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime eduCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime eduModified;



}
