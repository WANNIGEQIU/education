package com.galaxy.cloud.controller;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.galaxy.cloud.config.ConstantProperties;
import com.galaxy.cloud.feign.UserFegin;
import com.galaxy.common.util.LocalDateTimeUtils;
import com.galaxy.common.util.ResultCommon;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/Oss")
public class FileUploadController {

        @Autowired
        private UserFegin userFeign;
    /**
     * 上传头像
     * 1 获取上传文件 MultiparFile
     * 2 获取 文件名称 ,输入流
     */
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    String endpoint = ConstantProperties.ENDPOINT;
    // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
    String accessKeyId = ConstantProperties.ACCESSKEYID;
    String accessKeySecret = ConstantProperties.ACCESSKEYSECRET;
    String yourBucketName = ConstantProperties.YOURBUCJETNAME;

    @PostMapping("/uploadHead")
    public ResultCommon uploadHead(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "abc",required = false)String abc) {


        try {
            String filename = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();

            filename = uuid + filename;
            //有无abc参数有就上传到封面文件夹没有上传到头像文件
            abc =  StringUtils.isEmpty(abc) ? "封面": "头像";
            //获取当前时间 格式化yyyy/MM/dd
            String time = LocalDateTimeUtils.formatOther("yyyyMMdd");
            filename = time + "/"+abc+"/" +filename;
            System.out.println(filename);


            InputStream is = file.getInputStream();
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                       // InputStream inputStream = new FileInputStream("<yourlocalFile>");
            com.aliyun.oss.model.ObjectMetadata metadata = new com.aliyun.oss.model.ObjectMetadata();
            metadata.setContentType("image/jpg");
            ossClient.putObject(yourBucketName, filename, is,metadata);
            // 关闭OSSClient。
            ossClient.shutdown();
            // 拼接路径 https://edu-test123.oss-cn-beijing.aliyuncs.com/2020/01/15/abc/04f8835a-e91f-4f46-bf06-6f144c0a20e1timg.jpg
            String path = ConstantProperties.ADDRESS+"/"+filename;
            log.info("路径地址: [{}]",path);
            return ResultCommon.resultOk(path);
        } catch (IOException e) {
            e.printStackTrace();
            return  ResultCommon.resultFail();
        }

    }

    /**
     * 腾讯云oss不用了
     * @param file
     * @return
     */
    @PostMapping("/uploadoss")
    public ResultCommon tencentoss(@RequestParam("file") MultipartFile file){
        // 1 初始化用户身份信息（secretId, secretKey）。
       String secretId = ConstantProperties.ACCESSKEYID;
        String secretKey = ConstantProperties.ACCESSKEYSECRET;
        String bucket = ConstantProperties.YOURBUCJETNAME; //存储桶名称，格式：BucketName-APPID
        String region =  ConstantProperties.ENDPOINT;
        InputStream is = null;
        try {
            String filename = file.getOriginalFilename();

             is = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置输入流长度为500
            //objectMetadata.setContentLength(500);
        // 设置 Content type, 默认是 application/octet-stream
            //objectMetadata.setContentType("application/pdf");
            Region regions = new Region(region);
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            ClientConfig clientConfig = new ClientConfig(regions);
            COSClient cosClient = new COSClient(cred, clientConfig);
            PutObjectResult putObjectResult = cosClient.putObject(bucket, filename, is, objectMetadata);
           // String etag = putObjectResult.getETag();
           // log.info("md5: [{}]",etag);
            // https://heimao-1301106277.cos.ap-beijing-fsi.myqcloud.com/qa.jpg
            String path = "https://"+bucket+".cos."+regions.getRegionName()+".myqcloud.com/"+filename;
          //  log.info("路径: [{}]",path);

            return  ResultCommon.resultOk(path);
            // 关闭输入流..

        } catch (IOException e) {
            e.printStackTrace();
            return ResultCommon.resultFail();
        }finally {
            try {
                if (is !=null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }






    }

    /**
     * 用户头像上传
     */
    @PostMapping("/avatar")
    public ResultCommon upAvatar(@RequestParam("file") MultipartFile file,
                                 @RequestParam("username") String username) {
        System.out.println(username);
        //获取文件上传名字
        String filename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        filename = uuid + filename;
        //获取时间
        String time = LocalDateTimeUtils.formatOther("yyyy/MM/dd");
        filename = time + "/user/" + filename;
        System.out.println(filename);

        try {
            InputStream inputStream = file.getInputStream();
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // InputStream inputStream = new FileInputStream("<yourlocalFile>");
            com.aliyun.oss.model.ObjectMetadata metadata = new com.aliyun.oss.model.ObjectMetadata();
            metadata.setContentType("image/jpg");
            ossClient.putObject(yourBucketName, filename, inputStream,metadata);
            // 关闭OSSClient。
            ossClient.shutdown();
            String path = ConstantProperties.ADDRESS+"/"+filename;
            log.info("路径地址: [{}]",path);
            //boolean b = this.userFeign.upAvatar(path, username);
            return ResultCommon.resultOk(path);
        } catch (IOException e) {
            e.printStackTrace();
            return  ResultCommon.resultFail();

        }


    }

}
