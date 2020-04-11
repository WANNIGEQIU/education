package com.galaxy.base.service;

import com.galaxy.base.entity.EduRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-08
 */
public interface EduRoleService extends IService<EduRole> {

    Map<String, Object> findRoleByUserId(String userId);

    void saveUserRoleRealtionShip(Integer userId, Integer[] roleId);

    List<EduRole> selectRoleByUserId(Integer id);


}
