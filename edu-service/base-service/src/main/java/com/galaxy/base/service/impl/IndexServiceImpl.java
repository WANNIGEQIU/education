package com.galaxy.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.galaxy.base.entity.EduAdmin;
import com.galaxy.base.entity.EduRole;
import com.galaxy.base.service.EduAdminService;
import com.galaxy.base.service.EduMenuService;
import com.galaxy.base.service.EduRoleService;
import com.galaxy.base.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IndexServiceImpl implements IndexService {


    @Autowired
    private EduAdminService userService;

    @Autowired
    private EduRoleService roleService;

    @Autowired
    private EduMenuService permissionService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Map<String, Object> getUserInfo(String username) {

        Map<String, Object> result = new HashMap<>();
        EduAdmin eduAdmin = userService.selectByUsername(username);
        if ( eduAdmin == null) {

        }
        //根据用户id获取角色
        List<EduRole> roleList = roleService.selectRoleByUserId(eduAdmin.getId());
        List<String> roleNameList = roleList.stream().map(item -> item.getName()).collect(Collectors.toList());
        if(roleNameList.size() == 0) {
            //前端框架必须返回一个角色，否则报错，如果没有角色，返回一个空角色
            roleNameList.add("");
        }
        //根据用户id获取操作权限值
        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(eduAdmin.getId());
        //redisTemplate.opsForValue().set(username, permissionValueList);
        result.put("name", eduAdmin.getUsername());
        result.put("avatar", "https://edu-test123.oss-cn-beijing.aliyuncs.com/20200403/封面/a596fa25-4b4e-4923-9ae0-530ac19b394833eae38a-6907-488f-ae8e-1ddca8572de0file.jpeg");
        result.put("roles", roleNameList);
        result.put("permissionValueList", permissionValueList);
        return result;

    }

    @Override
    public List<JSONObject> getMenu(String username) {

        EduAdmin eduAdmin = userService.selectByUsername(username);
        //根据用户id获取用户菜单权限
        List<JSONObject> permissionList = permissionService.selectPermissionByUserId(eduAdmin.getId());

        return permissionList;
    }
}
