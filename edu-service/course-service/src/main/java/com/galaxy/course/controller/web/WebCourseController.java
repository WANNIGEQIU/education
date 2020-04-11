package com.galaxy.course.controller.web;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.common.bean.PageVo;
import com.galaxy.common.bean.UserBean;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.util.JwtUtil;
import com.galaxy.common.util.ResultCommon;
import com.galaxy.course.entity.EduCourse;
import com.galaxy.course.entity.EduUcourse;
import com.galaxy.course.entity.dto.TwoSubjectDto;
import com.galaxy.course.entity.dto.VideoDto;
import com.galaxy.course.service.EduCategoryService;
import com.galaxy.course.service.EduCourseService;
import com.galaxy.common.bean.CourseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course/web")

public class WebCourseController {
        @Autowired
        private EduCourseService courseService;

        @Autowired
        private EduCategoryService categoryService;


    /**
     * 添加购买记录
     */
    @PostMapping("/bought")
    public ResultCommon bought(String courseid, String username){
        Integer bought = this.courseService.bought(courseid, username);
        return ResultCommon.resultOk(bought);


    }



    /**
     * 课程分页列表
     * @param page
     * @param limit
     * @return
     */
    @PostMapping("/{page}/{limit}")
    public ResultCommon getCourseList(
            @PathVariable Long page, @PathVariable Long limit,
            @RequestBody(required = false) CourseVo bean) {
        Page<EduCourse> p = new Page<>(page, limit);


        Map courseList = courseService.getCourseList(p, bean);
        return ResultCommon.resultOk(courseList);

    }

    /**
     * 查询课程基本描述讲师分类章节小节
     * @param id 课程id
     * @return
     */
     @GetMapping("/details/{id}")
    public ResultCommon getCourseDetails(@PathVariable String id,HttpServletRequest request) {

         // 是否学习此课程
         UserBean jwt = JwtUtil.getJwt(request);
          if (jwt != null){
              String username = jwt.getUsername();
              Integer integer = this.courseService.queryStudy(username, id);
              Map courseDetails = courseService.getCourseDetails(id);
              courseDetails.put("study",integer);
          return ResultCommon.resultOk(courseDetails);
          }
          return ResultCommon.resultFail().codeAndMsg(ResultEnum.AUTH_ERROR_USER);

     }

    /**
     * 查询一级类别下的二级类别和课程信息
     * @return
     */
    @GetMapping("/courseinfo/{page}/{limit}/{oneid}")
        public ResultCommon queryCourseInfo(
                @PathVariable Long page,
                @PathVariable Long limit,
                @PathVariable("oneid") String id){

        Page<EduCourse> returnPage = new Page<>(page, limit);
        Map map = courseService.queryCourseInfo(returnPage, id);
        return ResultCommon.resultOk(map);

    }

    /**
     * 查询一级二级类别名称 和二级类别下的课程信息
     * @param page
     * @param limit
     * @param id
     * @return
     */
    @GetMapping("/couseinfo2/{page}/{limit}/{twoid}")
    public ResultCommon queryCourseInfo1(
            @PathVariable Long page,
            @PathVariable Long limit,
            @PathVariable("twoid") String id) {
        Page<EduCourse> returnPage = new Page<>(page, limit);
        Map map = courseService.queryCourseInfo1(returnPage, id);
        return ResultCommon.resultOk(map);


    }

    /**
     * 查询热门课程
     * @return
     */
    @GetMapping("/Popular")
    public ResultCommon queryPopularCourse() {
        List<EduCourse> eduCourses = courseService.queryPopularCourse();
        if (eduCourses.size() >0) {
            return ResultCommon.resultOk(eduCourses);
        }else {
            return ResultCommon.resultFail().data(ResultEnum.NO_POPULAR_COURSE);
        }
    }

    /**
     *  获取课程小节视频id凭证
     * @param  id 视频id
     * @return  课程名称 视频播放地址
     */
    @GetMapping("/videoinfo/{id}")
    public ResultCommon queryVideoInfo(@PathVariable String id) {
        VideoDto resource = courseService.queryVideoInfo(id);


        return ResultCommon.resultOk(resource);
    }

    /**
     * 根据课程id 和用户名查询此用户是否购买
     *
     **/

    @GetMapping("/ucbuy")
    public ResultCommon userIsBuy(String cid, HttpServletRequest request){


//        String token = request.getHeader(eduConfig.getTokenName());
//        //未登录
//        if (StringUtils.isEmpty(token)|| "undefined".equals(token)) {
//            return ResultCommon.resultOk(CourseConstant.NO_LOGIN_IN);
//        }
//
//        Integer integer = this.courseService.userIsBuy(cid, token);
//      return ResultCommon.resultOk(integer);
        return null;
    }


    /**
     * 获取我的课程信息
     * @param
     * @return
     */
    @GetMapping("/mycourse/{page}/{limit}")
    public ResultCommon getMyCourse(
            @PathVariable long page,
            @PathVariable long limit,
            HttpServletRequest request) {
        UserBean jwt = JwtUtil.getJwt(request);
        if (jwt == null){
            return ResultCommon.resultFail().codeAndMsg(ResultEnum.AUTH_ERROR_USER);
        }
        Page<EduUcourse> bean = new Page<>(page, limit);
        PageVo myCourse = this.courseService.getMyCourse(bean, jwt.getUsername());

        return ResultCommon.resultOk(myCourse);


    }

    /**
     * 根据一级类别获取二级类别
     * @param firstId
     * @return
     */
    @GetMapping("/category/{firstId}")
    public ResultCommon getSecondListByFirsrId(@PathVariable String firstId) {
        List<TwoSubjectDto> list = this.courseService.getSecondListByFirsrId(firstId);
        return ResultCommon.resultOk(list);
    }

    /**
     * 获取课程
     */
    @GetMapping("/getcourse/{cid}")
    public ResultCommon getcourse(@PathVariable String cid) {
        CourseVo getcourse = this.courseService.getcourse(cid);
        return ResultCommon.resultOk(getcourse);
    }


    /**
     * 记录用户课程信息 报名学习
     *
     *
     */
    @PostMapping("/record")
    public ResultCommon record ( String cid ,HttpServletRequest request) {

        UserBean jwt = JwtUtil.getJwt(request);
        if (jwt== null){
            return ResultCommon.resultFail().codeAndMsg(ResultEnum.AUTH_ERROR_USER);
        }

        Integer record = this.courseService.record(cid, jwt);
        return ResultCommon.resultOk(record);

    }







}
