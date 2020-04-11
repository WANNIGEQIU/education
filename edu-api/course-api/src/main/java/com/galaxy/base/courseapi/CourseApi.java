package com.galaxy.base.courseapi;

import com.galaxy.common.bean.CourseBean;
import com.galaxy.common.bean.CourseVo;
import com.galaxy.common.util.ResultCommon;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CourseApi {

    /**
     * 根据讲师id查询课程信息
     * @param
     * @return
     */
    @GetMapping("/course/queryids/{id}")
    List<CourseVo> queryIds(@PathVariable("id") String id);


    /**
     * 添加课程用户关联表
     * @param courseId username
     * @retrurn true ok
     */
    @PostMapping("/course/save/{courseId}/{username}")
    Boolean addUn(@PathVariable("courseId") String courseId,
                  @PathVariable("username") String username);

    @PostMapping("/course/aa/{aa}")
    Integer aa(@PathVariable("aa") String aa);

    @GetMapping("/course/courseNum/{day}")
    Integer queryCourse(@PathVariable("day") String day);


    @GetMapping("/course/lecturer")
    String queryForLect(@RequestParam("cid") String cid);


    @GetMapping("/course/orderquery/{courseId}")
    CourseBean orderquery(@PathVariable("courseId") String courseId);



    @GetMapping("/course/queryCategory")
    ResultCommon queryCategory(@RequestParam("categoryPid") String cate1,
                               @RequestParam("categoryId") String cate2);

    @GetMapping("/course/statistics/{day}")
    Long statisticsForCourse(@PathVariable("day") String day);

    /**
     * 添加购买记录
     */
    @PostMapping("/course/web/bought")
     ResultCommon bought(@RequestParam("courseid") String courseid,
                         @RequestParam("username") String username);
}
