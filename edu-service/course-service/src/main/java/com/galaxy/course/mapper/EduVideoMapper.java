package com.galaxy.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galaxy.course.entity.EduVideo;
import com.galaxy.course.entity.dto.VideoDto;

import java.util.List;

/**
 * <p>
 * 课程视频 Mapper 接口
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-16
 */
public interface EduVideoMapper extends BaseMapper<EduVideo> {

    Integer seleteids(String vsearch);

    List<EduVideo> queryListForVideo(String id);

    VideoDto queryVideoInfo(String id);
}
