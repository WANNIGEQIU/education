package com.galaxy.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.common.bean.PageVo;
import com.galaxy.common.bean.UserBean;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.common.util.ResultCommon;
import com.galaxy.course.entity.EduComment;
import com.galaxy.course.mapper.EduCommentMapper;
import com.galaxy.course.service.EduCommentService;
import com.galaxy.common.bean.CourseVo;
import com.galaxy.course.entity.dto.CommentDto;
import com.galaxy.course.feign.MemberFeign;
import com.galaxy.base.memberapi.MemberBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EduCommentServiceImpl implements EduCommentService {

    @Autowired
    private EduCommentMapper commentMapper;

    @Autowired
    private MemberFeign memberFeign;

    @Override
    public PageVo getComment(Page<EduComment> pageBean, CourseVo courseVo) {

        Page<EduComment> commentPage = this.commentMapper.selectPage(pageBean, new LambdaQueryWrapper<EduComment>()
                .eq(courseVo.getId() != null, EduComment::getCourseId, courseVo.getId())
                .orderByDesc(EduComment::getEduCreate));
        return new PageVo(commentPage);
    }

    @Override
    public Integer saveComment(CommentDto bean, UserBean jwt) {
        String id = jwt.getId();
        ResultCommon member = this.memberFeign.getMember(id);
        MemberBean memberBean = JSON.parseObject(JSON.toJSONString(member.getData()),
                new TypeReference<MemberBean>() {
        });

        if (memberBean == null) {
            log.error("远程调用失败：{}", JSON.toJSONString(member));
            throw new MyException(ResultEnum.MEMBER_INFO_ERROR);
        }
        EduComment eduComment = new EduComment();
        BeanUtils.copyProperties(bean,eduComment);
         eduComment.setUsername(memberBean.getUsername()).setAvatar(memberBean.getAvatar()).setMemberId(memberBean.getId()) ;
         int insert = this.commentMapper.insert(eduComment);

        return insert;
    }
}
