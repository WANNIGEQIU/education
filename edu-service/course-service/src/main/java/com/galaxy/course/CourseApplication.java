package com.galaxy.course;


import com.galaxy.common.annotation.MySpringCloud;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;


@MapperScan("com.galaxy.course.mapper")
@Slf4j
@MySpringCloud
public class CourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseApplication.class,args);
        log.info("课程微服务启动成功 ");

    }
}
