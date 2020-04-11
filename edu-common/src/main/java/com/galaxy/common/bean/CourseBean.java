package com.galaxy.common.bean;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseBean {

    private String id;

    private String lecturerId;

    private String categoryPid;

    private String categoryId;

    private String title;

    private BigDecimal price;

    private Integer lessonNum;

    private String cover;

    private String description;

    private String lecturerName;

    private String oneCategory;

    private String twoCategory;

    private String status;

    private String career;

    private Long buyCount;

    private Long viewCount;

    private String head;

    private String level;

    private String intro;



}
