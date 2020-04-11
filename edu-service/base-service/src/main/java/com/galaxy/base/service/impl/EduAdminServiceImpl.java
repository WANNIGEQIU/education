package com.galaxy.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.galaxy.base.entity.EduAdmin;
import com.galaxy.base.mapper.EduAdminMapper;
import com.galaxy.base.service.EduAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-08
 */
@Service
public class EduAdminServiceImpl extends ServiceImpl<EduAdminMapper, EduAdmin> implements EduAdminService {

    @Override
    public EduAdmin selectByUsername(String username) {


        return this.baseMapper.selectOne(new LambdaQueryWrapper<EduAdmin>()
                .eq(EduAdmin::getUsername,username));
    }
}
