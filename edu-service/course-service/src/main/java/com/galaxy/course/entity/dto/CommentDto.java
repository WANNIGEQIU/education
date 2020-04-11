package com.galaxy.course.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class CommentDto  implements Serializable{

    private Long id;
    private String content;
    private String memberId;
    private String courseId;
    private String teacherId;
    private String username;
    private String avatar;
}
