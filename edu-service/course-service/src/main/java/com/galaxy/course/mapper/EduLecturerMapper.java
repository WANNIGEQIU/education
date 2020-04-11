package com.galaxy.course.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.course.entity.EduLecturer;
import com.galaxy.course.entity.dto.LecturerDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 Mapper 接口
 * </p>
 *
 * @author 玩你个球儿
 * @since 2019-12-26
 */
public interface EduLecturerMapper extends BaseMapper<EduLecturer> {


   Page<EduLecturer> queryCondtion(Page page, LecturerDto lecturerDto);
   Page<EduLecturer> queryDeleted(Page page);
   int realDelete(String id);
   int recoverLecturer(Map map);

    List<EduLecturer> queryPopularLecturer();
}
