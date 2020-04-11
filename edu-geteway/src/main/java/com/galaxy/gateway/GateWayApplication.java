package com.galaxy.gateway;


import com.galaxy.common.annotation.MySpringCloud;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@Slf4j
@EnableZuulProxy
@MySpringCloud
public class GateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class,args);
        log.info("网关启动成功");
    }
}
