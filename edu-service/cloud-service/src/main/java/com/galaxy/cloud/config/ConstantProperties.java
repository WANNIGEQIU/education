package com.galaxy.cloud.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 服务启动时读取配置
 */
@Component
public class ConstantProperties implements InitializingBean {
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.yourBucketName}")
    private String yourBucketName;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.address}")
    private String address;

    @Value("${tencent.video.secretId}")
    private String secretId;

    @Value("${tencent.video.secretKey}")
    private String secretKey;

    public static String ACCESSKEYID;

    public static String ACCESSKEYSECRET;

    public static String YOURBUCJETNAME;

    public static String ENDPOINT;

    public static String ADDRESS;

    // 腾讯云 key
    public static String SECRETID;

    public static String SECRETKEY;


    /**
     * 服务启动时调用此方法
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESSKEYID = accessKeyId;
        ACCESSKEYSECRET = accessKeySecret;
        YOURBUCJETNAME = yourBucketName;
        ENDPOINT = endpoint;
        ADDRESS = address;
        SECRETID = secretId;
        SECRETKEY = secretKey;
    }
}
