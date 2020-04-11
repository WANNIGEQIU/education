package com.galaxy.cloud.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class VodConfig {

    /**
     * 服务端路径
     */
    @Value("${vod.path}")
    private String path;


}
