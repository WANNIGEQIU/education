package com.galaxy.base.controller;


import com.alibaba.fastjson.JSONObject;
import com.galaxy.base.service.IndexService;
import com.galaxy.common.bean.UserBean;
import com.galaxy.common.util.JwtUtil;
import com.galaxy.common.util.ResultCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/index")
public class IndexController {


    @Autowired
    private IndexService indexService;

    /**
     * 根据token获取用户信息
     */
    @GetMapping("info")
    public ResultCommon info(String token){
        //获取当前登录用户用户名
            UserBean jwtForAdmin = JwtUtil.getJwtForAdmin(token);
            String  username= jwtForAdmin.getUsername();



        // String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> userInfo = indexService.getUserInfo(username);
        return ResultCommon.resultOk(userInfo);
    }

    /**
     * 获取菜单
     * @return
     */
    @GetMapping("menu")
    public ResultCommon getMenu(HttpServletRequest request){
        //获取当前登录用户用户名
       // String username = SecurityContextHolder.getContext().getAuthentication().getName();

             UserBean jwtForAdmin = JwtUtil.getJwtForAdmin(request);
             String  username = jwtForAdmin.getUsername();



        List<JSONObject> permissionList = indexService.getMenu(username);

        return ResultCommon.resultOk(permissionList);
    }

    @PostMapping("logout")
    public ResultCommon logout(){
        return ResultCommon.resultOk();
    }


}
