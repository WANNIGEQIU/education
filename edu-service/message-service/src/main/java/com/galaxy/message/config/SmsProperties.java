package com.galaxy.message.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class SmsProperties implements InitializingBean {

     @Value("${heimao.message.accessKeyId}")
     private  String accessKeyId;

     @Value("${heimao.message.accessKeySecret}")
     private String accessKeySecret;

     @Value("${heimao.message.signName}")
     private String signName;

     @Value("${heimao.message.messageTemplate}")
     private String messageTemplate;

     public static String ACCESSKEYID;

     public static String ACCESSKEYSECRET;

     public static String SIGNNAME;

     public static String MESSAGETEMPLATE;

     @Override
     public void afterPropertiesSet() throws Exception {
          ACCESSKEYID = accessKeyId;
          ACCESSKEYSECRET = accessKeySecret;
          SIGNNAME = signName;
          MESSAGETEMPLATE = messageTemplate;

     }
}
