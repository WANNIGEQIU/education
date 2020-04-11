package com.galaxy.message.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.galaxy.message.config.SmsProperties;
import lombok.extern.slf4j.Slf4j;

/*
pom.xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.0.3</version>
</dependency>
*/


@Slf4j
public class SendSms {




    /**
     * 发短信功能
     * @param mobilePhone
     * @param code
     * @return
     */
    public static CommonResponse sendSms(String mobilePhone,String code) {

            log.info("阿里云短信: {},{}",mobilePhone,code);

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", SmsProperties.ACCESSKEYID, SmsProperties.ACCESSKEYSECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        try {
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobilePhone);
        request.putQueryParameter("SignName", SmsProperties.SIGNNAME);
        request.putQueryParameter("TemplateCode", SmsProperties.MESSAGETEMPLATE);
        request.putQueryParameter("TemplateParam", "{\"code\":"+code+"}");

            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());

            return response;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return new CommonResponse();

    }





    public static void main(String[] args) {

        //sendSms("18946303556","998821");


//        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",SmsProperties.ACCESSKEYID, SmsProperties.ACCESSKEYSECRET);
//        IAcsClient client = new DefaultAcsClient(profile);
//
//        CommonRequest request = new CommonRequest();
//        request.setMethod(MethodType.POST);
//        request.setDomain("dysmsapi.aliyuncs.com");
//        request.setVersion("2017-05-25");
//        request.setAction("SendSms");
//        request.putQueryParameter("RegionId", "cn-hangzhou");
//        request.putQueryParameter("PhoneNumbers", "18946303556");
//        request.putQueryParameter("SignName", "黑猫教育");
//        request.putQueryParameter("TemplateCode", "SMS_182684378");
//        String code = "231443";
//        request.putQueryParameter("TemplateParam", "{\"code\":"+code+"}");
//        try {
//            CommonResponse response = client.getCommonResponse(request);
//            System.out.println(response.getData());
//            //{"Message":"OK","RequestId":"D852E4D0-3585-497F-8EA1-71D369AE7D42","BizId":"450807580298048199^0","Code":"OK"}
//        } catch (ServerException e) {
//            e.printStackTrace();
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
    }


}
