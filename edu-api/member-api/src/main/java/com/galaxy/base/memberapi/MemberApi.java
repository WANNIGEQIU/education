package com.galaxy.base.memberapi;

import com.galaxy.common.bean.UserBean;
import com.galaxy.common.util.ResultCommon;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface MemberApi {

    /**
     * feign
     * 获取指定日期 用户的注册人数
     ** @return
     */
    @GetMapping("/user/querynums/{day}")
    public Integer queryNums(@PathVariable("day") String day);


    /**
     * 查询用户是否存在 根据用户名或手机号和密码
     */
    @GetMapping("/user/isexist")
    public UserBean queryUserIsexist(@RequestParam String account, @RequestParam String password);




    /**
     * 更新头像
     * @param path
     * @param username
     * @return
     */
    @PostMapping("/avatar/{path}/{username}")
    boolean upAvatar(
            @PathVariable("path") String path,
            @PathVariable("username") String username);


    @PostMapping("/user/member")
     ResultCommon getMember(@RequestParam("memberid") String memberid);


    /**
     * 增加用户积分
     */
    @PostMapping("/user/integral")
     ResultCommon UpdateIntegral(@RequestParam("userId") String userId,@RequestParam("integral") String integral);
}
