package com.galaxy.course.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.common.bean.PageVo;
import com.galaxy.common.bean.UserBean;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.util.JwtUtil;
import com.galaxy.common.util.ResultCommon;
import com.galaxy.course.entity.EduComment;
import com.galaxy.course.service.EduCommentService;
import com.galaxy.common.bean.CourseVo;
import com.galaxy.course.entity.dto.CommentDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/comment")
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;

    @PostMapping("/get/{page}/{limit}")
    public ResultCommon getComment(@PathVariable Long page, @PathVariable Long limit,
                                   @RequestBody(required = false)CourseVo courseVo){

        if (page == null || limit == null){
                page = 1L; limit =5L;
        }
        Page<EduComment> pageBean = new Page<>(page, limit);
        PageVo comment = this.commentService.getComment(pageBean, courseVo);
        return ResultCommon.resultOk(comment);
    }


    @ApiOperation(value = "添加评论")
    @PostMapping("/save")
    public ResultCommon saveComment(@RequestBody CommentDto commentDto, HttpServletRequest request) {

        UserBean jwt = JwtUtil.getJwt(request);
        if (jwt == null) {
            return ResultCommon.resultFail().codeAndMsg(ResultEnum.AUTH_ERROR_USER);
        }
        Integer integer = this.commentService.saveComment(commentDto, jwt);
        return ResultCommon.resultOk(integer);
    }

}
