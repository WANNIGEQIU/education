package com.galaxy.course.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChapterDto {
    private String id;

    @ApiModelProperty(value = "章节名称")
    private String title;

    private Integer sort;

    @ApiModelProperty(value = "小节集合")
    private List<VideoDto> children = new ArrayList<>();
}
