package com.galaxy.course.service;




import com.galaxy.course.entity.EduVideo;
import com.galaxy.course.entity.dto.VideoDto;

import java.util.Map;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-16
 */
public interface EduVideoService  {

    Boolean saveVideo(EduVideo eduVideo);

    Map queryOne(String id);

    Boolean updateVideo(VideoDto videoDto);

    Boolean deleteVideo(String id);
}
