package com.galaxy.cloud.service;


import com.galaxy.cloud.entity.AliVideoInfoDto;
import com.galaxy.cloud.entity.videoInfoBean;
import com.galaxy.common.bean.BaseVideoDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VideoService {
    /**
     * 返回fileId
     * @param file
     * @return
     */
    Integer uploadTencentCloud(MultipartFile file, String id) throws IOException;

    void deleteMedia(String id);

    BaseVideoDto queryVideInfo(String vid);

    List<videoInfoBean> select();


    AliVideoInfoDto getVideoInfo(String id);

    String getVideoPlay(String id);

    String getVideoAuth(String videoSourceId);

    void uploadForTencent(MultipartFile file, String id);
}

