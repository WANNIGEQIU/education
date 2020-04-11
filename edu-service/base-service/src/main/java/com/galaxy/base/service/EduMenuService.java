package com.galaxy.base.service;

import com.alibaba.fastjson.JSONObject;
import com.galaxy.base.entity.EduMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-08
 */
public interface EduMenuService extends IService<EduMenu> {



    List<EduMenu> queryAllMenu();

    void removeChildById(Integer id);

    void doAssign(Integer roleId, Integer[] menuIds);

    List<EduMenu> selectAllMenu(String roleId);

    List<String> selectPermissionValueByUserId(Integer id);
    List<JSONObject> selectPermissionByUserId(Integer id);


}
