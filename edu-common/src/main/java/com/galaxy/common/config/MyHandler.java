package com.galaxy.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动添加策略
 */
@Component
public class MyHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //添加时

        this.setFieldValByName("eduCreate",LocalDateTime.now(),metaObject);
        this.setFieldValByName("eduModified",LocalDateTime.now(),metaObject);
        this.setFieldValByName("did",false,metaObject);




    }

    @Override
    public void updateFill(MetaObject metaObject) {

        this.setFieldValByName("eduModified",LocalDateTime.now(),metaObject);


    }
}
