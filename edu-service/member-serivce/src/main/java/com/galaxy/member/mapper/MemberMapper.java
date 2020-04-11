package com.galaxy.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.member.entity.EduUser;

public interface MemberMapper extends BaseMapper<EduUser> {
    Page<EduUser> deleteUserList(Page<EduUser> objectPage);

    Integer queryNums(String day);
}
