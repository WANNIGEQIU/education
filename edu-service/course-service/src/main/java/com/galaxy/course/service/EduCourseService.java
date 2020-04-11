package com.galaxy.course.service;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.course.entity.EduCourse;
import com.galaxy.common.bean.CourseBean;
import com.galaxy.common.bean.CourseVo;
import com.galaxy.common.bean.PageVo;
import com.galaxy.common.bean.UserBean;
import com.galaxy.course.entity.dto.CourseInfoDto;
import com.galaxy.course.entity.dto.TwoSubjectDto;
import com.galaxy.course.entity.dto.VideoDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
public interface EduCourseService  {
    /**
     * 根据讲师id查询课程信息
     * @param
     * @return
     */
    List<CourseVo> selectCouse8Lid(String id);

    String saveCouseInfo(CourseInfoDto info);

    List a();

    CourseInfoDto getCourseInfo(String id);

    Boolean uploadCourse(CourseInfoDto info);

    PageVo selectCondtion(Page p, CourseInfoDto info);

    Boolean courseDelete(String id);

    CourseInfoDto queryInfo(String id);

    Boolean statePut(String id);


    Boolean changeStatus(String id, String status);

    Map getCourseList(Page<EduCourse> p, CourseVo bean);

    Map getCourseDetails(String id);

    Map queryCourseInfo(Page<EduCourse> returnPage, String id);

    Map queryCourseInfo1(Page<EduCourse> returnPage, String id);

    List<EduCourse> queryPopularCourse();

    VideoDto queryVideoInfo(String id);

    Integer userIsBuy(String cid, String token);

    CourseVo getOrderInfo(String cid);

    Boolean addUcourse(String courseId, String username);

    PageVo getMyCourse(Page page, String username);

    Integer queryCourse(String day);

   List<TwoSubjectDto> getSecondListByFirsrId(String firstId);

    CourseVo getcourse(String cid);

    Integer record(String cid, UserBean jwt);

    void delete(String[] ids);

    String queryForLect(String cid);

    CourseBean orderquery(String courseId);

    Long statisticsForCourse(String day);

    Integer queryStudy(String username, String id);

    Integer bought(String courseid, String username);
}

