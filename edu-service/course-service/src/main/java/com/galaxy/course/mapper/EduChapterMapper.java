package com.galaxy.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galaxy.course.entity.EduChapter;

import java.util.List;

/**
 * <p>
 * 课程章节 Mapper 接口
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-16
 */
public interface EduChapterMapper extends BaseMapper<EduChapter> {


    List<EduChapter> queryListForChapter(String id);
}
