package com.galaxy.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.galaxy.base.entity.EduAdmin;
import com.galaxy.base.entity.EduAdminRole;
import com.galaxy.base.entity.EduRole;
import com.galaxy.base.mapper.EduRoleMapper;
import com.galaxy.base.service.EduAdminRoleService;
import com.galaxy.base.service.EduRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class EduRoleServiceImpl extends ServiceImpl<EduRoleMapper, EduRole> implements EduRoleService {

    @Autowired
    private EduAdminRoleService adminRoleService;

    @Override
    public Map<String, Object> findRoleByUserId(String userId) {

        // 全部角色
        List<EduRole> eduRoles = this.baseMapper.selectList(null);
        // 从中间表获取 roleId
        List<EduAdminRole> role_admin_roleId = this.adminRoleService.list(new LambdaQueryWrapper<EduAdminRole>()
                .eq(EduAdminRole::getUserId, userId).select(EduAdminRole::getRoleId));
        List<Integer> role_ids = role_admin_roleId.stream().map(m -> m.getRoleId()).collect(Collectors.toList());

        // 筛选
        List<EduRole> result_list = new ArrayList<>();
        eduRoles.forEach(r -> {
            if (role_ids.contains(r.getId())) {
                result_list.add(r);
            }
        });
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoles", result_list);
        roleMap.put("allRolesList", eduRoles);

        return roleMap;
    }

    @Override
    public void saveUserRoleRealtionShip(Integer userId, Integer[] roleId) {

        adminRoleService.remove(new LambdaQueryWrapper<EduAdminRole>()
        .eq(EduAdminRole::getUserId,userId));

        List<EduAdminRole> userRoleList = new ArrayList<>();
        for(Integer s : roleId) {
            if(StringUtils.isEmpty(roleId)) continue;
            EduAdminRole adminRole = new EduAdminRole();
            adminRole.setUserId(userId).setRoleId(s);

            userRoleList.add(adminRole);
        }
        adminRoleService.saveBatch(userRoleList);
    }

    @Override
    public List<EduRole> selectRoleByUserId(Integer id) {
        //根据用户id拥有的角色id
        List<EduAdminRole> userRoleList = this.adminRoleService.list(new LambdaQueryWrapper<EduAdminRole>()
                .eq(EduAdminRole::getUserId, id).select(EduAdminRole::getRoleId));
        List<Integer> roleIdList = userRoleList.stream().map(m -> m.getRoleId()).collect(Collectors.toList());
        List<EduRole> roleList = new ArrayList<>();
        if(roleIdList.size() > 0) {
            roleList = baseMapper.selectBatchIds(roleIdList);
        }
        return roleList;
    }
}
