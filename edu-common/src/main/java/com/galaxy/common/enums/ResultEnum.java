package com.galaxy.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {

    SUCCESS(666,"操作成功!"),
    ERROR(999,"操作失败"),
    NO_ROOT(888,"没有相应权限"),
    EXCEPTION(777,"出现了异常"),
    REDIS_CONNECTION_EXCEPTION(7798,"Redis连接异常"),
    ZERO_EXCEPTION(900,"除0异常"),
    PARAM_ERROR(555,"参数不正确 !"),
    DELETE_ERROR(222,"删除失败"),
    QUERY_ERROR(333,"查询失败! 返回结果为NULL"),
    NO_NAME(745,"讲师姓名输入不正确!"),
    NO_LEVEL(746,"讲师等级错误"),
    NO_INTRODU(747,"讲师介绍错误"),
    IMPORT_EXCEL(333,"导入失败异常"),
    COURSE_SAVE_ERROR(111,"添加课程基本信息失败"),
    NO_COURSE_DESC(543,"没有填写课程描述"),
    NO_COURSE_TITLE(544,"没有填写课程名称"),
    NO_COURSE_LECTURER(544,"没有选择讲师"),
    NO_COURSE_CATEGORY(532,"没有选择课程类别"),
    NO_COURSE_INFO(533,"没有相关课程信息"),
    FAIL_COURSE_UPLOAD(522,"课程修改失败"),
    ERROR_COURSE_DELETE(512,"课程小节删除异常"),
    ERROR_CHAPTER_DELETE(556,"删除失败，请先删除章节下面的小节视频"),
    NO_VIDEO_CHAPTERID(578,"章节id为null"),
    NO_VIDEO_COURSEID(589,"课程id为null"),
    NO_VIDEO_TITLE(590,"视频小节名称为null"),
    EXCEPTION_SERVER(80032,"服务器繁忙请稍后再试"),
    ERROR_NO_TIME(1213,"没有传入时间"),
    NO_POPULAR_COURSE(2212,"暂时没有热门课程"),
    NO_POPULAR_LECTURER(3324,"没有查询到热门讲师"),
    USER_NO_ACCOUNT(9921,"请输入用户名,或手机号"),
    USER_NO_PHONE(9933,"请输入手机号"),
    VIDEO_ERROR_DELETE(3238,"视频删除失败"),
    AUTH_ERROR_USER(7777,"身份认证失败,请重新登录"),
    USER_EROR_LOGIN(32321,"用户名或密码错误"),
    USER_NO_PASSWORD(3320,"请输入密码"),
    USER_NO_USERNAME(3321,"请输入用户名"),
    USER_PASSWORD_ERROR(3323,"密码输入错误"),
    USER_IS_EXIST(1231,"用户已存在,请登录"),
    USER_NOT_EXIST(1321,"用户不存在 请注册"),
    USER_IS_PROHIBIT(3328,"账号已被禁止使用"),
    USER_PHONE_ISEXIST(78323,"该手机号已注册请登录"),
    VERFYICODE_IS_ERROR(3239,"验证码不正确"),
    VERFYRCODE_IS_NULL(3249,"验证码为null"),
    ORDER_SAVE_ERROR(3829,"创建订单失败,请稍后再试"),
    CHECK_PASSWORD_ERROR(9372,"输入的密码不一致"),
    AUTH_IS_GUOQI(50008,"验证信息已过期,请重新登录"),
    VIDEO_INFO_ERROR(5009,"获取视频信息失败"),
    MEMBER_NOT_LOGIN(3333,"您还没有登录,请登录查看"),
    MEMBER_INFO_ERROR(4421,"获取会员信息失败")

    ;

    private Integer code;
    private String message;

}
