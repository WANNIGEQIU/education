package com.galaxy.course.service;


import com.galaxy.course.entity.EduChapter;
import com.galaxy.course.entity.dto.ChapterDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程章节 服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-16
 */
public interface EduChapterService {

    Map<String, List<ChapterDto>> queryChapter(String courseId);

    Boolean createChapter(EduChapter chapter);

    HashMap<String, Object> selectOne(String id);

    Integer updateChaptet(ChapterDto chapter);

    Boolean deleteChapter(String chapterId);


}
