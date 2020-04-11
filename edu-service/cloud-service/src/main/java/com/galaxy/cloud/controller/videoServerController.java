package com.galaxy.cloud.controller;



import com.galaxy.cloud.entity.AliVideoInfoDto;
import com.galaxy.cloud.entity.videoInfoBean;
import com.galaxy.cloud.service.VideoService;
import com.galaxy.common.bean.BaseVideoDto;
import com.galaxy.common.exception.MyException;
import com.galaxy.common.util.ResultCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/videoup")
public class videoServerController {

    @Autowired
    private VideoService videoService;


    /**
     * 腾讯云视频上传
     */
    @PostMapping("/upload")
    public ResultCommon uploadForTencent(@RequestParam("file") MultipartFile file,
                                         @RequestParam String id){

        this.videoService.uploadForTencent(file, id);
        return ResultCommon.resultOk();

    }


    /**
     * 腾讯云上传视频 不用了
     * @param file
     * @return
     * @throws IOException
     */

    public ResultCommon uploadTencentCloud (@RequestParam("file") MultipartFile file,
                                            @RequestParam String id) throws IOException {
        Integer fileId = videoService.uploadTencentCloud(file,id);

        if (fileId == 1) {
            return ResultCommon.resultOk("视频已在后台上传");
        }else {
            return ResultCommon.resultFail().data("视频上传失败");
        }
    }

    /**
     * 腾讯云删除视频
     */
    @PostMapping("/media/{videoSourceId}")
    public ResultCommon DeleteMedia(@PathVariable("videoSourceId")String id) {

         videoService.deleteMedia(id);
        return ResultCommon.resultOk("腾讯云视频删除成功");

    }

    /**
     * 查询腾讯云视频基本信息内容
     * @param vid 视频id凭证
     *
     */
    @PostMapping("/queryvideo/{vid}")
    public ResultCommon queryVideInfo(@PathVariable String vid) throws MyException {
        BaseVideoDto BaseVideoDto = videoService.queryVideInfo(vid);

        return ResultCommon.resultOk(BaseVideoDto);
    }


    /**
     * 查询腾讯云所有视频资源
     */
    @PostMapping("/select")
    public ResultCommon select() {

        List<videoInfoBean> select = this.videoService.select();
       return ResultCommon.resultOk(select);
    }



    /**
     * 阿里云 获取视频信息
     * @param id  视频凭证
     */
    @GetMapping("/getVideoInfo")
    public ResultCommon getVideoInfo(String id){
        AliVideoInfoDto videoInfo = this.videoService.getVideoInfo(id);
        return ResultCommon.resultOk(videoInfo);
    }

    /**
     * 阿里云 获取播放信息
     */
    @GetMapping("/getVideoPlay")
    public ResultCommon getVideoPlay(String videoSourceId){
        String videoPlay = this.videoService.getVideoPlay(videoSourceId);
        return ResultCommon.resultOk(videoPlay);
    }

    /**
     * 阿里云获取播放凭证
     */
    @GetMapping("/getVideoAuth")
    public ResultCommon getVideoAuth(String videoSourceId){
        String videoAuth = this.videoService.getVideoAuth(videoSourceId);
        return ResultCommon.resultOk(videoAuth);

    }

}
