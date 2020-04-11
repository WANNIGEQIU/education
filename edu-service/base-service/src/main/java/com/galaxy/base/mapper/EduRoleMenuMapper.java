package com.galaxy.base.mapper;

import com.galaxy.base.entity.EduRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-08
 */
public interface EduRoleMenuMapper extends BaseMapper<EduRoleMenu> {

    void removeAll(@Param("roleId") Integer roleId, @Param("menuIds") Integer[] menuIds);
}
