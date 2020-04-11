package com.galaxy.course.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.common.bean.CourseBean;
import com.galaxy.course.entity.EduCourse;
import com.galaxy.course.entity.EduUcourse;
import com.galaxy.course.entity.dto.CourseCondtionDto;
import com.galaxy.course.entity.dto.CourseInfoDto;
import com.galaxy.common.bean.CourseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    CourseInfoDto getCourseInfo(String id);

    Page<CourseCondtionDto> selectCondtion(Page<CourseCondtionDto> p, CourseInfoDto info);

    CourseInfoDto queryInfo(String id);

    List<CourseVo> selectCourse8Lid(String id);

    List<EduCourse> queryPopularCourse();

    int userIsBuy(String cid, String username);

    int addUcourse(Map map);

    List<EduUcourse> getMyCourse1(String username);

    Integer queryCourse(String day);

    Page<EduCourse> selectMyCourse(Page page, String username);

    Integer findCid(String username, String cid);

    Integer record(@Param("id") String id, @Param("cid") String cid, @Param("username") String username);

    CourseBean orderquery(@Param("courseId") String courseId);

    CourseVo selectBycourseL(@Param("cid") String cid);

    void deleteStudyId(@Param("cid") String cid, @Param("username") String username);
}


