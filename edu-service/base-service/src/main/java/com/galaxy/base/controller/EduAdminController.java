package com.galaxy.base.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.base.entity.EduAdmin;
import com.galaxy.base.service.EduAdminService;
import com.galaxy.base.service.EduRoleService;
import com.galaxy.common.MD5;
import com.galaxy.common.bean.PageVo;
import com.galaxy.common.bean.UserBean;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.common.util.JwtUtil;
import com.galaxy.common.util.ResultCommon;
import io.jsonwebtoken.JwtBuilder;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-08
 */
@RestController
@RequestMapping("/admin")
public class EduAdminController {

    @Autowired
    private EduAdminService userService;

    @Autowired
    private EduRoleService roleService;

    @ApiOperation(value = "获取管理用户分页列表")
    @GetMapping("{page}/{limit}")
    public ResultCommon index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                    EduAdmin userQueryVo) {
        Page<EduAdmin> pageParam = new Page<>(page, limit);
        LambdaQueryWrapper<EduAdmin> wrapper = new LambdaQueryWrapper<EduAdmin>()
                .like(!StringUtils.isEmpty(userQueryVo.getUsername()), EduAdmin::getUsername, userQueryVo.getUsername());
        Page<EduAdmin> pageModel = userService.page(pageParam, wrapper);
        PageVo<EduAdmin> pageVo = new PageVo<>(pageModel);
        return ResultCommon.resultOk(pageVo);
    }

    @ApiOperation(value = "新增管理用户")
    @PostMapping("save")
    public ResultCommon save(@RequestBody EduAdmin user) {
        user.setPassword(MD5.encrypt(user.getPassword()));
        userService.save(user);
        return ResultCommon.resultOk();
    }

    @ApiOperation(value = "修改管理用户")
    @PutMapping("update")
    public ResultCommon updateById(@RequestBody EduAdmin user) {
        userService.updateById(user);
        return ResultCommon.resultOk();
    }

    @ApiOperation(value = "删除管理用户")
    @DeleteMapping("remove/{id}")
    public ResultCommon remove(@PathVariable String id) {
        userService.removeById(id);
        return ResultCommon.resultOk();
    }

    @ApiOperation(value = "根据用户id查询信息")
    @GetMapping("get/{id}")
    public ResultCommon get(@PathVariable String id) {
        EduAdmin one = userService.getOne(new LambdaQueryWrapper<EduAdmin>().eq(EduAdmin::getId, id));
        one.setEduCreate(null).setEduModified(null);
        Map<String, Object> map = new HashMap<>();
        map.put("item",one);
        return ResultCommon.resultOk(map);
    }

    @ApiOperation(value = "根据id列表删除管理用户")
    @DeleteMapping("batchRemove")
    public ResultCommon batchRemove(@RequestBody List<String> idList) {
        userService.removeByIds(idList);
        return ResultCommon.resultOk();
    }

    @ApiOperation(value = "根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public ResultCommon toAssign(@PathVariable String userId) {
        Map<String, Object> roleMap = roleService.findRoleByUserId(userId);
        return ResultCommon.resultOk(roleMap);
    }

    @ApiOperation(value = "根据用户分配角色")
    @PostMapping("/doAssign")
    public ResultCommon doAssign(@RequestParam Integer userId,@RequestParam Integer[] roleId) {
        roleService.saveUserRoleRealtionShip(userId,roleId);
        return ResultCommon.resultOk();
    }


    @PostMapping("/login")
    public ResultCommon login(@RequestBody EduAdmin dto){
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(dto.getUsername())) {
            throw new MyException(ResultEnum.USER_EROR_LOGIN);
        }
        EduAdmin eduAdmin = this.userService.selectByUsername(dto.getUsername());

        // 开发者管理员

         if (eduAdmin.getUsername().equals(dto.getUsername()) && eduAdmin.getPassword().equals(MD5.encrypt(dto.getPassword()))) {
             String tokenSecret = JwtUtil.getTokenSecret(eduAdmin.getId(), eduAdmin.getUsername());
             map.put("token",tokenSecret);
         }
        return ResultCommon.resultOk(map);
    }


    @GetMapping("/info")
    public ResultCommon info(@RequestParam String token) {
        System.out.println("token-admin: "+token);
        Map<String, Object> map = new HashMap<>();

        UserBean jwtForAdmin = JwtUtil.getJwtForAdmin(token);
        String username = jwtForAdmin.getUsername();
        map.put("name","root");
        map.put("roles","[Root]");
        map.put("avatar","https://edu-test123.oss-cn-beijing.aliyuncs.com/2020/01/13/33eae38a-6907-488f-ae8e-1ddca8572de0file.png");

        return ResultCommon.resultOk(map);
    }

}

