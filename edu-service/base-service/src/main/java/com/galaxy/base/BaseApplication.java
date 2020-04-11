package com.galaxy.base;

import com.galaxy.common.annotation.MySpringCloud;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

@MapperScan("com.galaxy.base.mapper")
@MySpringCloud
public class BaseApplication {

    private final static Logger logger = LoggerFactory.getLogger(BaseApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class,args);
        logger.info("基础服务启动成功");
    }
}
