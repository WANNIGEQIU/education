package com.galaxy.cloud.service;


import com.alibaba.fastjson.JSON;
import com.galaxy.cloud.config.ConstantProperties;
import com.galaxy.common.bean.BaseVideoDto;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AsyncService {


    public BaseVideoDto baseVideoInfo(String vid) {
        BaseVideoDto baseVideoDto = new BaseVideoDto();
        String infoName = null;
        try {
            Credential cred = new Credential(ConstantProperties.SECRETID, ConstantProperties.SECRETKEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            VodClient client = new VodClient(cred, "", clientProfile);
            String params = "{\"FileIds\":[" + vid + "]}";
            DescribeMediaInfosRequest req = DescribeMediaInfosRequest.fromJsonString(params, DescribeMediaInfosRequest.class);
            DescribeMediaInfosResponse resp = null;
            resp = client.DescribeMediaInfos(req);
            MediaInfo[] mediaInfoSet = resp.getMediaInfoSet();
            for (MediaInfo mediaInfo : mediaInfoSet) {
                // 视频基础信息
                MediaBasicInfo basicInfo = mediaInfo.getBasicInfo();
                //视频名称
                infoName = basicInfo.getName();
                MediaTranscodeInfo transcodeInfo = mediaInfo.getTranscodeInfo();
                MediaTranscodeItem[] transcodeSet = transcodeInfo.getTranscodeSet();
                for (MediaTranscodeItem item : transcodeSet) {
                    baseVideoDto.setDuration(item.getDuration());
                    baseVideoDto.setSize(item.getSize());

                }
            }
            baseVideoDto.setTitle(infoName);


            log.info("视频信息: {}", JSON.toJSONString(baseVideoDto));
        } catch (TencentCloudSDKException e) {
            String localizedMessage = e.getLocalizedMessage();
            System.out.println(localizedMessage);
        }
        return baseVideoDto;
    }
}
