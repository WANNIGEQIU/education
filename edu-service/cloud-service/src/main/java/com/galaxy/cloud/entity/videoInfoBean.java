package com.galaxy.cloud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class videoInfoBean {

    private String name;
    private  String fileId;
    private  String description;
    private String createTime;
    private String updateTime;
    private String type;
    private String  storageRegion;

}
