package com.galaxy.course.controller;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.common.bean.CourseBean;
import com.galaxy.common.bean.CourseVo;
import com.galaxy.common.bean.PageVo;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.common.util.LocalDateTimeUtils;
import com.galaxy.common.util.ResultCommon;
import com.galaxy.course.entity.dto.CourseCondtionDto;
import com.galaxy.course.entity.dto.CourseInfoDto;
import com.galaxy.course.service.EduCategoryService;
import com.galaxy.course.service.EduCourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
@RestController
@RequestMapping("/course")
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduCategoryService categoryService;

    /**
     *
     *      根据讲师id查询课程信息 web feign讲师
     *
     */
    @GetMapping("/queryids/{id}")
    public List<CourseVo> queryIds (@PathVariable String id) {
        List<CourseVo> courseVos = courseService.selectCouse8Lid(id);
        return courseVos;
    }


    /**
     * 添加课程用户关联表
     * @param courseId username
     * @retrurn true ok
     */
    @PostMapping("/save/{courseId}/{username}")
    public Boolean addUn(@PathVariable String courseId, @PathVariable String username) {
        if (org.springframework.util.StringUtils.isEmpty(courseId) && org.springframework.util.StringUtils.isEmpty(username)) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }

        Boolean result = this.courseService.addUcourse(courseId, username);
        if (true) {
            return result;
        }else {
            return !result;
        }
    }
    @GetMapping("/lecturer")
    public String queryForLect( String cid) {

        String s = this.courseService.queryForLect(cid);
        return s;
    }

    @GetMapping("/courseNum/{day}")
    public Integer queryCourse(@PathVariable String day) {

        if (StringUtils.isEmpty(day)) {
            day = LocalDateTimeUtils.formatOther("yyyy-MM-dd");
        }

        Integer integer = this.courseService.queryCourse(day);
        return integer;
    }

    @GetMapping("/orderquery/{courseId}")
    public CourseBean orderquery(@PathVariable String courseId) {

        return this.courseService.orderquery(courseId);

    }

    @GetMapping("/queryCategory")
    public ResultCommon queryCategory(@RequestParam("categoryPid") String cate1,
                                      @RequestParam("categoryId") String cate2){

        CourseVo courseVo = this.categoryService.queryCategory(cate1, cate2);
        return ResultCommon.resultOk(courseVo);

    }


    @GetMapping("/statistics/{day}")
    public Long statisticsForCourse(@PathVariable String day){

        Long aLong = this.courseService.statisticsForCourse(day);
        return aLong;
    }


    /**
     * 根据课程id查询讲师课程基本和描述分类信息
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public ResultCommon queryInfo(@PathVariable String id) {
        CourseInfoDto courseInfoDto = courseService.queryInfo(id);
        return ResultCommon.resultOk(courseInfoDto);
    }



    /**
     * 添加课程信息
     * @return
     */
    @PostMapping("/save")
    public ResultCommon saveCouseInfo(@RequestBody CourseInfoDto info) {
        if (StringUtils.isEmpty(info.getTitle())) {
            throw new MyException(ResultEnum.NO_COURSE_TITLE);
        }
        if (StringUtils.isEmpty(info.getLecturerId())) {
            throw new MyException(ResultEnum.NO_COURSE_LECTURER);
        }
        if(StringUtils.isEmpty(info.getCategoryId())) {
            throw new MyException(ResultEnum.NO_COURSE_CATEGORY);
        }
        String s = courseService.saveCouseInfo(info);
        return ResultCommon.resultOk(s);

    }

    /**
     * 查询课程信息和描述信息
     * @param id
     * @return
     */
     @GetMapping("/courseInfo/{id}")
     public ResultCommon getCourseInfo(@PathVariable("id") String id) {
         CourseInfoDto courseInfoDto = courseService.getCourseInfo(id);
         return ResultCommon.resultOk(courseInfoDto);
     }


    /**
     * 课程基本和课程描述更新
     * @param info
     * @return
     */
    @PutMapping("/upload")
     public ResultCommon upload(@RequestBody CourseInfoDto info) {
        Boolean r = this.courseService.uploadCourse(info);
        if (r) {
            return ResultCommon.resultOk();
        }else {
            return ResultCommon.resultFail();
        }
    }

    /**
     * 课程分页
     * @return
     */
    @GetMapping("/condtion")
    public ResultCommon courseCondtion(CourseInfoDto info){
            if (info.getPage() == null || info.getPage() < 0) {
                info.setPage(1L);
            }
            if (info.getLimit() == null || info.getLimit() < 0) {
                info.setLimit(20L);
            }
        Page<CourseCondtionDto> p = new Page<>(info.getPage(),info.getLimit());
        PageVo pageVo = courseService.selectCondtion(p, info);

        return ResultCommon.resultOk(pageVo);
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/{id}")
    public ResultCommon courseDelete(@PathVariable("id") String id) {
        Boolean delete = courseService.courseDelete(id);
        if (delete) {
            return ResultCommon.resultOk();
        }else {
            return ResultCommon.resultFail();
        }

    }

    /**
     * 批量删除课程
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public ResultCommon delete(@RequestBody String []ids){

          this.courseService.delete(ids);
        return ResultCommon.resultOk();
    }


    @PutMapping("/state/{id}")
    public ResultCommon statePut(@PathVariable String id) {
        Boolean put = courseService.statePut(id);
        if (put) {
            return ResultCommon.resultOk(put);
        }else {
            return ResultCommon.resultFail().data(put);
        }
    }


    @PutMapping("/change/{id}/{status}")
    public ResultCommon changeStatus(@PathVariable String id,@PathVariable String status) {
        Boolean r = courseService.changeStatus(id, status);
        if (r) {
            return ResultCommon.resultOk();
        }else {
            return ResultCommon.resultFail();
        }
    }


    /**
     * 根据cid 查询订单所需要的信息内容
     * @param cid 课程id
     * @return
     */
    @GetMapping("/loadorder/{cid}")
    public ResultCommon getOrderInfo(@PathVariable String cid) {
        if (StringUtils.isEmpty(cid)){
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
        CourseVo orderInfo = this.courseService.getOrderInfo(cid);
        return ResultCommon.resultOk(orderInfo);
    }















}

