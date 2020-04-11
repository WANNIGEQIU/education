package com.galaxy.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.galaxy.base.entity.EduAdmin;
import com.galaxy.base.entity.EduMenu;
import com.galaxy.base.entity.EduRoleMenu;
import com.galaxy.base.entity.vo.MenuVo;
import com.galaxy.base.helper.MemuHelper;
import com.galaxy.base.helper.PermissionHelper;
import com.galaxy.base.mapper.EduMenuMapper;
import com.galaxy.base.service.EduAdminService;
import com.galaxy.base.service.EduMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.galaxy.base.service.EduRoleMenuService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.reflection.ArrayUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-08
 */
@Service
public class EduMenuServiceImpl extends ServiceImpl<EduMenuMapper, EduMenu> implements EduMenuService {


    @Autowired
    private EduRoleMenuService roleMenuService;

    @Autowired
    private EduAdminService adminService;

    @Override
    public List<EduMenu> queryAllMenu() {
        List<EduMenu> menus = this.baseMapper.selectList(new LambdaQueryWrapper<EduMenu>().orderByDesc(EduMenu::getId));
        List<EduMenu> list = buildMenu(menus);
        return list;
    }

    @Override
    public void removeChildById(Integer id) {

        List<Integer> list = new ArrayList<>();

        this.selectChildById(id,list);
        list.add(id);
        baseMapper.deleteBatchIds(list);





    }

    @Override
    public void doAssign(Integer roleId, Integer[] menuIds) {

        // 先删除 去重复
         roleMenuService.removeAll(roleId,menuIds);

        List<EduRoleMenu> list = new ArrayList<>();
        // 封装数据
        for (Integer menuId : menuIds) {
            EduRoleMenu roleMenu = new EduRoleMenu();
            roleMenu.setRoleId(roleId).setMenuId(menuId);
            list.add(roleMenu);
        }

        // 角色菜单关系表
        roleMenuService.saveBatch(list);


    }

    @Override
    public List<EduMenu> selectAllMenu(String roleId) {
        // 所有菜单
        List<EduMenu> menuList = this.baseMapper.selectList(new LambdaQueryWrapper<EduMenu>().orderByDesc(EduMenu::getId));

        // 根据角色id从中间表获取菜单
        List<EduRoleMenu> roleMenus = this.roleMenuService.list(new LambdaQueryWrapper<EduRoleMenu>()
                .eq(EduRoleMenu::getRoleId, roleId));
        // 中间表 菜单id集合
        List<Integer> role_menu_ids = roleMenus.stream().map(m -> m.getMenuId()).collect(Collectors.toList());
         menuList.forEach(ml -> {
             if (role_menu_ids.contains(ml.getId())) {
                ml.setSelect(true);
             }else {
                 ml.setSelect(false);
             }
         });
        List<EduMenu> eduMenus = buildMenu(menuList);

        return eduMenus;
    }
    //根据用户id获取用户菜单
    @Override
    public List<String> selectPermissionValueByUserId(Integer id) {

        List<String> selectPermissionValueList = null;
        if(this.isSysAdmin(id)) {
            //如果是系统管理员，获取所有权限
            selectPermissionValueList = baseMapper.selectAllPermissionValue();
        } else {
            selectPermissionValueList = baseMapper.selectPermissionValueByUserId(id);
        }
        return selectPermissionValueList;
    }

    @Override
    public List<JSONObject> selectPermissionByUserId(Integer id) {
        List<EduMenu> selectPermissionList = null;
        if(this.isSysAdmin(id)) {
            //如果是超级管理员，获取所有菜单
            selectPermissionList = baseMapper.selectList(null);
        } else {
            selectPermissionList = baseMapper.selectPermissionByUserId(id);
        }

        List<EduMenu> permissionList = PermissionHelper.bulid(selectPermissionList);
        List<JSONObject> result = MemuHelper.bulid(permissionList);
        return result;
    }

    private void selectChildById(Integer id,List<Integer> idList) {

        List<EduMenu> eduMenus = this.baseMapper.selectList(new LambdaQueryWrapper<EduMenu>()
                .eq(EduMenu::getMid, id)
                .select(EduMenu::getId));
        eduMenus.forEach(item->{
            idList.add(item.getId());
            this.selectChildById(item.getId(),idList);
        });

    }


    // 封装菜单
    private List<EduMenu> buildMenu(List<EduMenu> menus) {
        List<EduMenu> finalNode = new ArrayList<>(50);
         menus.forEach(node->{
             if (node.getMid() == 0){
                 node.setLevel(1);
                 // 查询子菜单
                 finalNode.add(selectChildren(node,menus));
             }
         });

        return finalNode;
    }

    private EduMenu selectChildren(EduMenu node, List<EduMenu> finalNode) {
        node.setChildren(new ArrayList<>());
        finalNode.forEach(f->{
            if (node.getId().equals(f.getMid())) {
                // 下一级
                int i = node.getLevel() + 1;
                f.setLevel(i);
                if (node.getChildren() == null) {
                    node.setChildren(new ArrayList<>());
                }
                node.getChildren().add(selectChildren(f,finalNode));


            }
        });

        return node;


    }

    /**
     * 判断用户是否系统管理员
     * @param userId
     * @return
     */
    private boolean isSysAdmin(Integer userId) {
        EduAdmin user = adminService.getById(userId);

        if(null != user && "admin".equals(user.getUsername())) {
            return true;
        }
        return false;
    }
}
