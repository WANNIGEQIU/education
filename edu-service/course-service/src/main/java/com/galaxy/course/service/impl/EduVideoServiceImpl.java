package com.galaxy.course.service.impl;



import com.galaxy.course.entity.EduVideo;
import com.galaxy.course.entity.dto.VideoDto;
import com.galaxy.course.feign.CloudFeign;
import com.galaxy.course.service.EduVideoService;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.course.mapper.EduVideoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-16
 */
@Service
@Slf4j
public class EduVideoServiceImpl  implements EduVideoService {

    @Autowired
    private EduVideoMapper videoMapper;

    @Autowired
    private CloudFeign cloudFeign;



    @Override
    public Boolean saveVideo(EduVideo eduVideo) {
        if (StringUtils.isEmpty(eduVideo.getChapterId())) {
            throw new MyException(ResultEnum.NO_VIDEO_CHAPTERID);
        }
        if (StringUtils.isEmpty(eduVideo.getCourseId())) {
            throw new MyException(ResultEnum.NO_VIDEO_COURSEID);
        }
        if (StringUtils.isEmpty(eduVideo.getTitle())) {
            throw new MyException(ResultEnum.NO_VIDEO_TITLE);
        }

        // 默认付费
        if (eduVideo.getIsFree() == null) {
            eduVideo.setIsFree(false);
        }
        int insert = videoMapper.insert(eduVideo);


        return insert>0;
    }

    @Override
    public Map queryOne(String id) {
        HashMap<String, Object> map = new HashMap<>();
        EduVideo eduVideo = videoMapper.selectById(id);
            if (eduVideo == null) {
                throw new MyException(10001,"数据显示失败");
            }
        map.put("result",eduVideo);


        return map;
    }

    @Override
    public Boolean updateVideo(VideoDto videoDto) {
         if (videoDto == null) {
             throw new MyException(ResultEnum.PARAM_ERROR);
         }
        EduVideo eduVideo = new EduVideo();
         BeanUtils.copyProperties(videoDto,eduVideo);
        int i = videoMapper.updateById(eduVideo);

        return i>0;
    }

    @Override
    public Boolean deleteVideo(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }

        EduVideo select = videoMapper.selectById(id);
        String videoSourceId = select.getVideoSourceId();
        //有视频就删除
        if (!StringUtils.isEmpty(videoSourceId)) {
             cloudFeign.DeleteMedia(videoSourceId);


        }

        return 0 < videoMapper.deleteById(id);


    }
}
