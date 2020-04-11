package com.galaxy.base.mapper;

import com.galaxy.base.entity.EduMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-08
 */
public interface EduMenuMapper extends BaseMapper<EduMenu> {

    List<String> selectAllPermissionValue();

    List<String> selectPermissionValueByUserId(Integer id);
    List<EduMenu> selectPermissionByUserId(Integer userId);




}
