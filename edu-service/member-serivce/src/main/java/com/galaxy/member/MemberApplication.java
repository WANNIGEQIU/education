package com.galaxy.member;

import com.galaxy.common.annotation.MySpringCloud;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;


@Slf4j
@MapperScan("com.galaxy.member.mapper")
@MySpringCloud
public class MemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class,args);
        log.info("会员微服务启动成功");
    }


//    @GetMapping("/hello")
//    public ResultCommon hello(String name){
//        System.out.println(name);
//        throw new MyException(ResultEnum.AUTH_ERROR_USER);
//    }

}
