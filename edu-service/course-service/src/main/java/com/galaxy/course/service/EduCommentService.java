package com.galaxy.course.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.common.bean.CourseVo;
import com.galaxy.common.bean.PageVo;
import com.galaxy.common.bean.UserBean;
import com.galaxy.course.entity.EduComment;
import com.galaxy.course.entity.dto.CommentDto;

public interface EduCommentService {
    PageVo getComment(Page<EduComment> pageBean, CourseVo courseVo);

    Integer saveComment(CommentDto commentDto, UserBean jwt);
}
