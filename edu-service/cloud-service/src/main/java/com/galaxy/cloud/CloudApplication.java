package com.galaxy.cloud;

import com.galaxy.common.annotation.MySpringCloud;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;

@Slf4j
@MySpringCloud
public class CloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class,args);
        log.info("云服务启动成功");
    }

}
