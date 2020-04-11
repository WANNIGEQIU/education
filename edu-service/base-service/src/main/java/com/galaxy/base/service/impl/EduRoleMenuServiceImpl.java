package com.galaxy.base.service.impl;

import com.galaxy.base.entity.EduRoleMenu;
import com.galaxy.base.mapper.EduRoleMenuMapper;
import com.galaxy.base.service.EduRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-08
 */
@Service
public class EduRoleMenuServiceImpl extends ServiceImpl<EduRoleMenuMapper, EduRoleMenu> implements EduRoleMenuService {

    @Override
    public void removeAll(Integer roleId, Integer[] menuIds) {
        this.baseMapper.removeAll(roleId,menuIds);

    }
}
