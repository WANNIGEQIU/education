package com.galaxy.order;


import com.galaxy.common.annotation.MySpringCloud;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

@MySpringCloud
@MapperScan("com.galaxy.order.mapper")
@Slf4j
public class OrderApplication  {

    public static void main(String[] args) {

        SpringApplication.run(OrderApplication.class);
        log.info("order 服务 启动成功");

    }


}
