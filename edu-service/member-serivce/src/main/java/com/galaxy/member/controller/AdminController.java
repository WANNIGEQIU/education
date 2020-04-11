package com.galaxy.member.controller;

import com.galaxy.common.util.ResultCommon;
import com.galaxy.member.entity.dto.UserDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {


   @PostMapping("/login")
    public ResultCommon login(@RequestBody UserDto dto){

       if ("root".equals(dto.getUsername()) && "root".equals(dto.getPassword())){
           Map<String, Object> map = new HashMap<>();
           map.put("token","token");
           return ResultCommon.resultOk(map);
       }
       return ResultCommon.resultOk();
   }


 @GetMapping("/info")
    public ResultCommon info(@RequestParam String token) {

     Map<String, Object> map = new HashMap<>();
     map.put("name","root");
     map.put("roles","[Root]");
     map.put("avatar","https://edu-test123.oss-cn-beijing.aliyuncs.com/2020/01/13/33eae38a-6907-488f-ae8e-1ddca8572de0file.png");

     return ResultCommon.resultOk(map);
 }


 @PostMapping("/logout")
    public ResultCommon logout(HttpServletRequest request, HttpServletResponse response){

     String header = request.getHeader("X-Token");
     System.out.println(header);
     response.setHeader("X-Token","");
     return ResultCommon.resultOk();
 }




}
