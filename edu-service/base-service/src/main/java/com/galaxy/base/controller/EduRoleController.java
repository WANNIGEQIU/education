package com.galaxy.base.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.base.entity.EduRole;
import com.galaxy.base.service.EduRoleService;
import com.galaxy.common.bean.PageVo;
import com.galaxy.common.util.ResultCommon;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-08
 */
@RestController
@RequestMapping("/role")
public class EduRoleController {

    @Autowired
    private EduRoleService roleService;

    @ApiOperation(value = "获取角色分页列表")
    @GetMapping("{page}/{limit}")
    public ResultCommon index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            EduRole role) {
        Page<EduRole> pageParam = new Page<>(page, limit);
        LambdaQueryWrapper wrapper = new LambdaQueryWrapper<EduRole>()
                .like(!StringUtils.isEmpty(role.getName()),EduRole::getName,role.getName());
        Page pageModel = roleService.page(pageParam, wrapper);
        PageVo pageVo = new PageVo<>(pageModel);

        return ResultCommon.resultOk(pageVo);
    }

    @ApiOperation(value = "获取角色")
    @GetMapping("get/{id}")
    public ResultCommon get(@PathVariable Integer id) {
        EduRole role = roleService.getById(id);
        role.setEduCreate(null).setEduModified(null);
        return ResultCommon.resultOk(role);
    }

    @ApiOperation(value = "新增角色")
    @PostMapping("save")
    public ResultCommon save(@RequestBody EduRole role) {
        roleService.save(role);
        return ResultCommon.resultOk();
    }

    @ApiOperation(value = "修改角色")
    @PutMapping("update")
    public ResultCommon updateById(@RequestBody EduRole role) {
        roleService.updateById(role);
        return ResultCommon.resultOk();
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("remove/{id}")
    public ResultCommon remove(@PathVariable Integer id) {
        roleService.removeById(id);
        return ResultCommon.resultOk();
    }

    @ApiOperation(value = "根据id列表删除角色")
    @DeleteMapping("batchRemove")
    public ResultCommon batchRemove(@RequestBody List<Integer> idList) {
        roleService.removeByIds(idList);
        return ResultCommon.resultOk();
    }

}

