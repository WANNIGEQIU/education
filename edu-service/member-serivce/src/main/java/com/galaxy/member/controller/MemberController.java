package com.galaxy.member.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.common.bean.PageVo;
import com.galaxy.common.bean.UserBean;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.common.util.JwtUtil;
import com.galaxy.common.util.ResultCommon;
import com.galaxy.member.entity.EduUser;
import com.galaxy.member.entity.dto.UserDto;
import com.galaxy.member.service.MemberService;
import com.galaxy.base.memberapi.MemberBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")

public class MemberController {


    @Autowired
    private MemberService memberService;


    @PostMapping("/member")
    public ResultCommon getMember(String memberid){
        MemberBean member = this.memberService.getMember(memberid);
        return ResultCommon.resultOk(member);

    }


    /**
     * 注册功能 data 1 注册成功 0 注册失败
     * @param bean
     * @param
     * @return
     */
    @PostMapping("/register")
    public ResultCommon UserRegister(UserDto bean) {
        String verfyCode;
        if (StringUtils.isEmpty(bean.getUsername())) {
            throw new MyException(ResultEnum.USER_NO_USERNAME);
        }
        if (StringUtils.isEmpty(bean.getPassword())) {
            throw new MyException(ResultEnum.USER_NO_PASSWORD);
        }
        if(StringUtils.isEmpty(bean.getMobile())) {
            throw new MyException(ResultEnum.USER_NO_PHONE);
        }
        if (StringUtils.isEmpty(bean.getVerifyCode())) {
            throw new MyException(32329,"验证码为空");
        }
        verfyCode = bean.getVerifyCode();
        int i = this.memberService.UserRegister(bean,verfyCode);
        if (i == 1 ) {
            return ResultCommon.resultOk(i);
        }else {
            return ResultCommon.resultFail().data(i);
        }


    }

    /**
     * 根据token获取用户
     * @param request
     * @return
     */
    @GetMapping("/getUserInfo")
    public ResultCommon checkToken(HttpServletRequest request) {

        UserBean userBean = JwtUtil.getJwt(request);
        return ResultCommon.resultOk(userBean);
    }

    /**
     * 登录
     *
     * @param account
     * @return
     */
    @PostMapping("/login")
    public ResultCommon userLogin(String account, String password) {

        if (StringUtils.isBlank(account)) {
            throw new MyException(ResultEnum.USER_NO_ACCOUNT);
        }
        if (StringUtils.isBlank(password)) {
            throw new MyException(ResultEnum.USER_NO_PASSWORD);
        }

        String token = this.memberService.userLogin(account, password);

        return ResultCommon.resultOk(token);

    }

    /**
     *
     */
    @GetMapping("/memberinfo")
    public ResultCommon memberInfo(HttpServletRequest request){

        UserBean jwt = JwtUtil.getJwt(request);
        if (jwt == null){
            return ResultCommon.resultFail().codeAndMsg(ResultEnum.AUTH_ERROR_USER);
        }

        EduUser eduUser = this.memberService.memberInfo(jwt.getId());
        return ResultCommon.resultOk(eduUser);
    }


    /**
     * 增加用户积分
     */
    @PostMapping("/integral")
     public ResultCommon UpdateIntegral(String userId,String integral){

        Integer integer = this.memberService.UpdateIntegral(userId, integral);
        return ResultCommon.resultOk(integer);

    }


    /**
     * 发送短信验证码
     *
     * @param mobile
     * @return
     */
    @PostMapping("/verifycode/{mobile}")
    public ResultCommon sendVerifyCode(@PathVariable String mobile) {

        memberService.sendVerifyCode(mobile);
        return ResultCommon.resultOk();

    }

    /**
     * 忘记密码 短信验证
     */
    @PostMapping("/lossCode/{mobile}")
    public ResultCommon lossCode(@PathVariable String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            throw new MyException(4394, "手机号不能为空");
        }
        this.memberService.lossCode(mobile);
        return ResultCommon.resultOk();
    }


    /**
     * 忘记密码 确认验证码手机号
     */
    @PostMapping("/lossPassword1")
    public ResultCommon lossPassword1(@RequestBody UserDto dto) {

        if (StringUtils.isEmpty(dto.getMobile())) {
            throw new MyException(3291, "手机号不能为空");
        }
        if (StringUtils.isEmpty(dto.getVerifyCode())) {
            throw new MyException(8233, "验证码不能为空");
        }
        Integer integer = this.memberService.lossPassword1(dto);
        return ResultCommon.resultOk(integer);

    }

    /**
     * 忘记密码 修改密码
     */
    @PostMapping("/lossPassword2")
    public ResultCommon lossPassword2(@RequestBody UserDto dto) {
        if (StringUtils.isEmpty(dto.getMobile())) {
            throw new MyException(3291, "手机号不能为空");
        }
        if (StringUtils.isEmpty(dto.getPassword())) {
            throw new MyException(3294, "密码不能为null");
        }
        if (StringUtils.isEmpty(dto.getCheckPassword())) {
            throw new MyException(8223, "请再次输入密码");
        }
        String pass1 = dto.getPassword();
        String pass2 = dto.getCheckPassword();
        String phone = dto.getMobile();

        boolean b = this.memberService.lossPassword2(pass1, pass2, phone);
        return ResultCommon.resultOk(b);

    }

    @PostMapping("/logout")
    public ResultCommon logout(HttpServletRequest request, HttpServletResponse response) {

        String header = request.getHeader("X-Token");
        System.out.println(header);
        response.setHeader("X-Token","");
        return ResultCommon.resultOk();

    }



    /**
     * 个人信息修改
     */
    @PostMapping("/updateUser")
    public ResultCommon updateUser(@RequestBody UserDto dto){


        boolean b = this.memberService.updateUser(dto);

         if (b){
             return ResultCommon.resultOk("信息修改成功!");
         }
        return ResultCommon.resultFail().data("信息修改失败! 请稍后再试");

    }

    /**
     * 更换头像
     */

    @PutMapping("/avatar")
    public ResultCommon upAvatar(String head,HttpServletRequest request){

        UserBean jwt = JwtUtil.getJwt(request);
        if (jwt == null){
            return ResultCommon.resultFail().codeAndMsg(ResultEnum.AUTH_ERROR_USER);
        }
        int i = this.memberService.upAvatar(jwt.getId(),head);
        return ResultCommon.resultOk(i);

    }


    /**
     * feign
     * 获取指定日期 用户的注册人数
     ** @return
     */
    @GetMapping("/querynums/{day}")
    public Integer queryNums(@PathVariable String day) {

        Integer integer = this.memberService.queryNums(day);
        return  integer == null? 0 : integer;
    }


    /**
     * 获取用户列表
     * @param page
     * @param limit
     * @param dto
     * @return
     *
     */
    @PostMapping("/user/{page}/{limit}")
    public ResultCommon getUserList(
            @PathVariable Long page,
            @PathVariable Long limit,
            @RequestBody(required = false) UserDto dto) {

        if (page < 0 || limit < 0) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }

        Page<EduUser> objectPage = new Page<>(page, limit);
        PageVo userList = this.memberService.getUserList(objectPage, dto);
        if (userList == null) {
            return ResultCommon.resultFail();
        }else {
            return ResultCommon.resultOk(userList);
        }
    }



    /**
     * 删除用户
     */
    @DeleteMapping("/user/{id}")
    public ResultCommon deleteUser(@PathVariable String id) {

        if (StringUtils.isEmpty(id)) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
        boolean b = this.memberService.deleteUser(id);
        if (b){
            return ResultCommon.resultOk(true);
        }else {
            return ResultCommon.resultFail().data(false);
        }
    }


    /**
     * 禁用用户
     */
    @PutMapping("/prohibit/{id}")
    public ResultCommon prohibitUser(@PathVariable String id) {
        if (StringUtils.isEmpty(id)) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
        boolean b = this.memberService.prohibitUser(id);
        if (b) {
            return ResultCommon.resultOk(true);
        }else {
            return ResultCommon.resultFail().data(false);
        }

    }

    /**
     * 恢复被禁用用户
     */

    @PutMapping("/recovery/{id}")
    public ResultCommon recoveryUser(@PathVariable String id) {
        if (StringUtils.isEmpty(id)) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
        boolean b = this.memberService.recoveryUser(id);
        if (b) {
            return ResultCommon.resultOk(true);
        }else {
            return ResultCommon.resultFail().data(false);
        }

    }

    /**
     * 已被删除用户列表
     */
    @PostMapping("/dellis/{page}/{limit}")
    public ResultCommon deleteUserList(@PathVariable long page,
                                       @PathVariable long limit) {
        if (page < 0 || limit < 0) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
        Page<EduUser> page1 = new Page<>(page, limit);
        PageVo<EduUser> pageVo = this.memberService.deleteUserList(page1);
        return ResultCommon.resultOk(pageVo);

    }

}
