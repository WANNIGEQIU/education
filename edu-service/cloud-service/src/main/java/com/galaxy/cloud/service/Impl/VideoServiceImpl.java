package com.galaxy.cloud.service.Impl;



import com.alibaba.fastjson.JSON;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.galaxy.cloud.config.ConstantProperties;
import com.galaxy.cloud.config.VodConfig;
import com.galaxy.cloud.entity.AliVideoInfoDto;
import com.galaxy.cloud.entity.videoInfoBean;
import com.galaxy.cloud.service.AsyncService;
import com.galaxy.cloud.service.VideoService;
import com.galaxy.cloud.utils.AInitVodCilent;
import com.galaxy.common.bean.BaseVideoDto;
import com.galaxy.common.util.LocalDateTimeUtils;

import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import com.tencentcloudapi.vod.v20180717.models.SearchMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.SearchMediaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoService videoService;


    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private VodConfig vodConfig;

    @Autowired
    private AsyncService asyncService;


    private void checkFile(File file) {
        if (file.getParentFile().exists()) {
            file.delete();
        } else {
            file.mkdirs();
        }
    }


    @Override
    public void uploadForTencent(MultipartFile file, String id) {
        BaseVideoDto baseVideoDto = new BaseVideoDto();
        log.info("视频id: {}", id);
        File serverPath = null;
        String fileId;
        try {
            String path = vodConfig.getPath();
            serverPath = new File(path + "/" + file.getOriginalFilename());
            log.info("服务端临时路径: {}", serverPath);
            checkFile(serverPath);
            System.out.println(LocalDateTime.now());
            file.transferTo(serverPath);
            System.out.println(LocalDateTime.now());
            VodUploadClient client = new VodUploadClient(ConstantProperties.SECRETID, ConstantProperties.SECRETKEY);
            VodUploadRequest request = new VodUploadRequest();
            request.setMediaFilePath(serverPath.getAbsolutePath());
            request.setMediaName(file.getOriginalFilename());
            // 文件分片并发数
            request.setConcurrentUploadNumber(5);
            VodUploadResponse response = client.upload("ap-guangzhou", request);
            // 腾讯云视频播放凭证
            fileId = response.getFileId();
            System.out.println("播放凭证："+fileId);
            if (fileId == null){
                return;
            }
            baseVideoDto.setId(id).setVideoId(fileId);
            BaseVideoDto asynsInfo = asyncService.baseVideoInfo(fileId);

                baseVideoDto.setDuration(asynsInfo.getDuration())
                        .setTitle(asynsInfo.getTitle())
                        .setSize(asynsInfo.getSize());



            this.rabbitTemplate.convertAndSend("VIDEO", "video", baseVideoDto);
            log.info("发送VOD--mq---> {}", JSON.toJSONString(baseVideoDto));
        } catch (Exception e) {
            serverPath.delete();
            log.info("视频上传异常id: {},异常信息: {}", id,e.getMessage());


        }


    }

    @Override
    public Integer uploadTencentCloud(MultipartFile file, String id) throws IOException {
        log.info("视频id: {}", id);
        File path = null;
        String fileId = null;
        Map<String, Object> hashMap = new HashMap<>();


        try {

            String filename = file.getOriginalFilename();
            path = new File(
                    "/Users/haha/demo/" + filename);
            if (path.getParentFile().exists()) {
                path.delete();
            } else {
                path.mkdirs();
            }

            file.transferTo(path);
            long size = file.getSize() / 1024 / 1024;
            System.out.println("大小---》" + size + "mb");


            VodUploadClient client = new VodUploadClient(ConstantProperties.SECRETID, ConstantProperties.SECRETKEY);
            VodUploadRequest request = new VodUploadRequest();
            //上传后的媒体名称，若不填默认采用 MediaFilePath 的文件名。
            filename = UUID.randomUUID().toString() + LocalDateTimeUtils.formatOther("yyyyMMdd") + filename;
            request.setMediaName("hello_" + filename);
            request.setMediaFilePath(path.getAbsolutePath());

            VodUploadResponse response = client.upload("ap-guangzhou", request);
            //视频凭证
            fileId = response.getFileId();
            if (!StringUtils.isEmpty(fileId)) {
                log.info("视频上传到腾讯云成功 {}", fileId);
                path.delete();
                // 发送mq
                Map info = null;
                info.put("proof", fileId);
                info.put("vid", id);
                this.rabbitTemplate.convertAndSend("VIDEO", "video", info);
                log.info("发送云mq---> 凭证{}, 视频id{}, 视频名称{}", fileId, id, JSON.toJSONString(info));
                return 1;


            }


        } catch (Exception e) {
            // 业务方进行异常处理
            path.delete();
            log.error("视频上传到腾讯云失败", e.getMessage());
            return 0;

        }

        return 0;


    }

    @Override
    public void deleteMedia(String id) {
        Map<String, Object> map = new HashMap<>();
        try {

            Credential cred = new Credential(ConstantProperties.SECRETID, ConstantProperties.SECRETKEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            VodClient client = new VodClient(cred, "", clientProfile);
            // String params = "{\"FileId\":\"5285890797884794546\"}";

            String params = "{\"FileId\":" + id + "}";
            log.info("视频id {}", params);
            DeleteMediaRequest req = DeleteMediaRequest.fromJsonString(params, DeleteMediaRequest.class);

            DeleteMediaResponse resp = client.DeleteMedia(req);


        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }


    }

    @Override
    public BaseVideoDto queryVideInfo(String vid) {
        BaseVideoDto baseVideoDto = new BaseVideoDto();
        String name = null;         // 文件名


        try {
            Credential cred = new Credential(ConstantProperties.SECRETID, ConstantProperties.SECRETKEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            VodClient client = new VodClient(cred, "", clientProfile);
            // String xx = "{\"FileIds\":[ "+vid+"]}";

            // String params = "{\"FileIds\":["+vid+"]}";
            String params = "{\"FileIds\":[" + vid + "]}";

            DescribeMediaInfosRequest req = DescribeMediaInfosRequest.fromJsonString(params, DescribeMediaInfosRequest.class);

            DescribeMediaInfosResponse resp = null;

            resp = client.DescribeMediaInfos(req);

            MediaInfo[] mediaInfoSet = resp.getMediaInfoSet();
            for (MediaInfo mediaInfo : mediaInfoSet) {
                // 视频基础信息
                MediaBasicInfo basicInfo = mediaInfo.getBasicInfo();
                //视频名称
                name = basicInfo.getName();
                MediaTranscodeInfo transcodeInfo = mediaInfo.getTranscodeInfo();
                MediaTranscodeItem[] transcodeSet = transcodeInfo.getTranscodeSet();
                for (MediaTranscodeItem item : transcodeSet) {
                    baseVideoDto.setDuration(item.getDuration());
                    baseVideoDto.setSize(item.getSize());

                }

            }
            baseVideoDto.setTitle(name);


            log.info("视频信息: {}", JSON.toJSONString(baseVideoDto));
            System.out.println(DescribeMediaInfosRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            String localizedMessage = e.getLocalizedMessage();
            System.out.println(localizedMessage);
        }


        return baseVideoDto;

    }

    @Override
    public List<videoInfoBean> select() {
        List<videoInfoBean> list = new ArrayList<>();


        try {

            Credential cred = new Credential(ConstantProperties.SECRETID, ConstantProperties.SECRETKEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            VodClient client = new VodClient(cred, "", clientProfile);

            String params = "{}";
            SearchMediaRequest req = SearchMediaRequest.fromJsonString(params, SearchMediaRequest.class);

            SearchMediaResponse resp = client.SearchMedia(req);
            MediaInfo[] mediaInfoSet = resp.getMediaInfoSet();
            for (MediaInfo mediaInfo : mediaInfoSet) {
                //凭证id
                String fileId = mediaInfo.getFileId();
                MediaBasicInfo basicInfo = mediaInfo.getBasicInfo();
                //视频名称
                String name = basicInfo.getName();
                //视频描述
                String description = basicInfo.getDescription();
                String createTime = basicInfo.getCreateTime();
                String updateTime = basicInfo.getUpdateTime();
                String type = basicInfo.getType();
                String storageRegion = basicInfo.getStorageRegion();
                videoInfoBean videoInfoBean = new videoInfoBean(name, fileId, description, createTime, updateTime, type, storageRegion);
                list.add(videoInfoBean);

            }

            System.out.println(SearchMediaRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            log.error("获取腾讯云存储视频信息失败");
            System.out.println(e.toString());
        }

        return list;
    }





    @Override
    public AliVideoInfoDto getVideoInfo(String id) {
        AliVideoInfoDto infoDto = new AliVideoInfoDto();
        GetVideoInfoRequest request = new GetVideoInfoRequest();
        request.setVideoId(id);
        try {
            DefaultAcsClient client = AInitVodCilent.initVodClient(ConstantProperties.ACCESSKEYID, ConstantProperties.ACCESSKEYSECRET);
            GetVideoInfoResponse response = new GetVideoInfoResponse();
            response = client.getAcsResponse(request);
            GetVideoInfoResponse.Video video = response.getVideo();
            if (video != null) {
                BeanUtils.copyProperties(video, infoDto);
            }

        } catch (ClientException e) {
            e.printStackTrace();
            log.error("ErrorMessage: {}", JSON.toJSONString(e.getLocalizedMessage()));
        }

        return infoDto;
    }

    @Override
    public String getVideoPlay(String id) {
        String source = "";

        try {
            DefaultAcsClient client = AInitVodCilent.initVodClient(ConstantProperties.ACCESSKEYID, ConstantProperties.ACCESSKEYSECRET);
            GetPlayInfoResponse response = new GetPlayInfoResponse();
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            request.setVideoId(id);
            response = client.getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            List<String> collect = playInfoList.stream().map(m -> m.getPlayURL()).collect(Collectors.toList());
            source = collect.get(0);
            System.out.println(source);

        } catch (ClientException e) {
            e.printStackTrace();
        }


        return source;
    }

    @Override
    public String getVideoAuth(String videoSourceId) {
        String playAuth = "";
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoSourceId);
        try {
            DefaultAcsClient client = AInitVodCilent.initVodClient(ConstantProperties.ACCESSKEYID, ConstantProperties.ACCESSKEYSECRET);
            // 播放凭证
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
            response = client.getAcsResponse(request);
            playAuth = response.getPlayAuth();

        } catch (Exception e) {

        }

        return playAuth;
    }


}
