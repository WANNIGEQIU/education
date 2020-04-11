package com.galaxy.base.service;

import com.galaxy.base.entity.EduAdmin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-08
 */
public interface EduAdminService extends IService<EduAdmin> {

    EduAdmin selectByUsername(String username);

}
