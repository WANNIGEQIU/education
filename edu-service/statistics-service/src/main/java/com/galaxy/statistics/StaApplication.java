package com.galaxy.statistics;


import com.galaxy.common.annotation.MySpringCloud;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

@MySpringCloud
@MapperScan("com.galaxy.statistics.mapper")
public class StaApplication {
    public static void main(String[] args) {
        SpringApplication.run(StaApplication.class,args);
        System.out.println("统计服务启动成功");
    }
}
