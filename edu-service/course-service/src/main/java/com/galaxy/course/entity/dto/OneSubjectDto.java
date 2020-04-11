package com.galaxy.course.entity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OneSubjectDto {

    private  String id;
    private  String title;
    List<TwoSubjectDto> children = new ArrayList();
}
