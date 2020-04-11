package com.galaxy.base.cloudapi;

import com.galaxy.common.exception.MyException;
import com.galaxy.common.util.ResultCommon;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface CloudApi {

    /**
     * 腾讯云删除视频
     */
    @PostMapping("/videoup/media/{videoSourceId}")
    void DeleteMedia(@PathVariable("videoSourceId") String id);


    /**
     * 阿里云 获取播放信息
     */
    @GetMapping("/videoup/getVideoPlay")
    ResultCommon getVideoPlay(@RequestParam("videoSourceId") String videoSourceId);

    /**
     * 阿里云获取播放凭证
     */
    @GetMapping("/videoup/getVideoAuth")
    ResultCommon getVideoAuth(@RequestParam("videoSourceId") String videoSourceId);

    /**
     * 查询腾讯云视频基本信息内容
     * @param vid 视频id凭证
     *
     */
    @PostMapping("/videoup/queryvideo/{vid}")
    ResultCommon queryVideInfo(@PathVariable("vid") String vid) throws MyException;
}
