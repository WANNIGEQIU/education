package com.galaxy.base.controller;


import com.galaxy.base.entity.EduMenu;
import com.galaxy.common.util.ResultCommon;
import com.galaxy.base.service.EduMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin/menu")
public class EduMenuController {

    @Autowired
    private EduMenuService menuService;


    //获取全部菜单
    @ApiOperation(value = "查询所有菜单")
    @GetMapping
    public ResultCommon indexAllMenu() {
        List<EduMenu> list =  menuService.queryAllMenu();
        Map<String, Object> map = new HashMap<>();
        map.put("children",list);
        return ResultCommon.resultOk(map);
    }

    @ApiOperation(value = "递归删除菜单")
    @DeleteMapping("remove/{id}")
    public ResultCommon remove(@PathVariable Integer id) {
        menuService.removeChildById(id);
        return ResultCommon.resultOk();
    }

    /**
     *
     * @param roleId  角色id
     * @param permissionId  菜单数组
     * @return
     */
    @ApiOperation(value = "给角色分配菜单")
    @PostMapping("/doAssign")
    public ResultCommon doAssign(Integer roleId,Integer[] permissionId) {
        menuService.doAssign(roleId,permissionId);
        return ResultCommon.resultOk();
    }

    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("toAssign/{roleId}")
    public ResultCommon toAssign(@PathVariable String roleId) {
        List<EduMenu> list = menuService.selectAllMenu(roleId);
        Map<String, Object> map = new HashMap<>();
        map.put("children",list);
        return ResultCommon.resultOk(map);
    }



    @ApiOperation(value = "新增菜单")
    @PostMapping("save")
    public ResultCommon save(@RequestBody EduMenu menu) {

        menuService.save(menu);
        return ResultCommon.resultOk();
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public ResultCommon updateById(@RequestBody EduMenu menu) {
        menuService.updateById(menu);
        return ResultCommon.resultOk();
    }
}

