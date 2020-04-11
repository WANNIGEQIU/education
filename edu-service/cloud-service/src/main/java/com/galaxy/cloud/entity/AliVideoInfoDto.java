package com.galaxy.cloud.entity;

import lombok.Data;

/**
 * 阿里视频返回信息
 * https://help.aliyun.com/document_detail/52839.html?spm=a2c4g.11186623.2.17.756b2d524wtSJZ#Video
 */
@Data
public class AliVideoInfoDto {


    private String VideoId;
    private String Title;
    private String Description;
    private Float Duration;
    private String CoverURL;
    private String Status;
    private String CreationTime;
    private Long Size;
    private String[] Snapshots;
    private Long CateId;
    private String CateName;
    private String Tags;
    private String TemplateGroupId;
    private String StroageLocation;
    private String AppId;
}
