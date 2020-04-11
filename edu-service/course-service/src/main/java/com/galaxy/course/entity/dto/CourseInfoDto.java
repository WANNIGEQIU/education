package com.galaxy.course.entity.dto;

import com.galaxy.common.bean.SearchBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)

public class CourseInfoDto extends SearchBean {

    @ApiModelProperty(value = "课程ID")
    private String id;

    @ApiModelProperty(value = "讲师id")
    private String lecturerId;

    @ApiModelProperty(value = "课程分类一级类别")
    private String categoryPid;

    @ApiModelProperty(value = "课程二级分类ID")
    private String categoryId;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程价格 0 免费")
    private BigDecimal price;

    @ApiModelProperty(value = "课时")
    private Integer lessonNum;

    @ApiModelProperty(value = "封面路径")
    private String cover;

    @ApiModelProperty(value = "课程简介")
    private String description;

    @ApiModelProperty(value = "讲师姓名")
    private String lecturerName;

    @ApiModelProperty(value = "一级类别名称")
    private String oneCategory;

    @ApiModelProperty(value = "二级类别名称")
    private String twoCategory;

    @ApiModelProperty(value = "课程状态")
    private String status;

    @ApiModelProperty(value = "讲师经历,学历,经验")
    private String career;

    @ApiModelProperty(value = "销售数量")
    private Long buyCount;

    @ApiModelProperty(value = "浏览数量")
    private Long viewCount;

    @ApiModelProperty(value = "讲师头像")
    private String head;

    @ApiModelProperty(value = "讲师头衔")
    private Integer level;

    @ApiModelProperty(value = "讲师简介")
    private String intro;

}
