package com.galaxy.course.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CourseCondtionDto {

    @ApiModelProperty("课程id")
    private String id;

    @ApiModelProperty(value = "课程讲师ID")
    private String lecturerId;

    @ApiModelProperty(value = "课程专业ID")
    private String categoryId;

    @ApiModelProperty(value = "课程分类一级类别")
    private String categoryPid;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "总课时")
    private Integer lessonNum;

    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;

    @ApiModelProperty(value = "销售数量")
    private Long buyCount;

    @ApiModelProperty(value = "浏览数量")
    private Long viewCount;

    @ApiModelProperty(value = "视频状态 Draft未发布  Normal已发布")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime eduCreate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime eduModified;

    @ApiModelProperty(value = "课程简介")
    private String description;

    //讲师id
    private String cid;
    //讲师名字
    private String name;

    private String intro;
    private String career;
    private Integer levele;

    private String oneCategory;
    private String twoCategory;
}
