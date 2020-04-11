package com.galaxy.course.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EduUcourse implements Serializable {

    private static final long serialVersionUID=1223L;


    private String id;

    private String username;

    private String courseId;
}
