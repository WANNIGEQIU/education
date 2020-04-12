package com.galaxy.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.common.bean.PageVo;
import com.galaxy.member.entity.EduUser;
import com.galaxy.member.entity.dto.UserDto;
import com.galaxy.base.memberapi.MemberBean;

public interface MemberService {
    Integer queryNums(String day);



    void sendVerifyCode(String mobile);

    int UserRegister(UserDto eduUser, String verifyCode);



    String userLogin(String account,String password);


    PageVo getUserList(Page<EduUser> objectPage, UserDto dto);

    boolean deleteUser(String id);

    boolean prohibitUser(String id);

    boolean recoveryUser(String id);

    PageVo<EduUser> deleteUserList(Page<EduUser> objectPage);

    EduUser getUserInfo(String username);

    Integer updatePoints(String username, String amount);

    void lossCode(String mobile);

    Integer lossPassword1(UserDto dto);

    boolean lossPassword2(String s1 ,String s2,String s3);

    boolean updateUser(UserDto dto);


    int upAvatar(String id,String head);

    MemberBean getMember(String memberid);

    Integer UpdateIntegral(String userId, String integral);

    EduUser memberInfo(String id);
}
