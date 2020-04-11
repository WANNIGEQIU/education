package com.galaxy.common.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CourseVo {

    private String id;

    private Integer sort ;

    private String description;

    private String lecturerId;
    private String lecturerName;

    private String categoryId;

    private String categoryPid;

    private String title;

    private String categoryName;

    private List  categoryList ;

    private String categoryPname;


    private BigDecimal price;

    private Integer lessonNum;

    private String cover;

    private Long buyCount;

    private Long viewCount;


    private String status;

    private String search;
}
