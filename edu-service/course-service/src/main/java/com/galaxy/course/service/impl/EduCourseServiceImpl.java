package com.galaxy.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.galaxy.common.bean.CourseBean;
import com.galaxy.common.bean.PageVo;
import com.galaxy.common.bean.UserBean;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.common.util.LocalDateTimeUtils;
import com.galaxy.common.util.MyString;
import com.galaxy.common.util.SnowFlake;
import com.galaxy.course.entity.*;
import com.galaxy.course.entity.dto.CourseInfoDto;
import com.galaxy.course.entity.dto.TwoSubjectDto;
import com.galaxy.course.entity.dto.VideoDto;
import com.galaxy.course.feign.CloudFeign;
import com.galaxy.common.bean.CourseVo;
import com.galaxy.common.constants.CourseConstant;
import com.galaxy.course.entity.dto.ChapterDto;
import com.galaxy.course.mapper.*;
import com.galaxy.course.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
@Service
@Slf4j
public class EduCourseServiceImpl implements EduCourseService {

    private Lock lock = new ReentrantLock();

    @Autowired
    private EduCourseMapper courseMapper;

    @Autowired
    private EduCourseDescriptionService descriptionService;

    @Autowired
    private EduCourseDescriptionMapper descriptionMapper;

    @Autowired
    private EduChapterMapper chapterMapper;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private EduVideoMapper videoMapper;

    @Autowired
    private EduLecturerMapper lecturerMapper;

    @Autowired
    private EduCategoryMapper categoryMapper;

    @Autowired
    private CloudFeign cloudFeign;

    @Autowired
    private EduCourseDescriptionMapper courseDescriptionMapper;


    @Autowired
    private EduVideoService videoService;

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private StringRedisTemplate redisTemplate;




    @Autowired
    private AsyncService asyncService;


    @Override
    public List<CourseVo> selectCouse8Lid(String id) {
        List<CourseVo> courseVos = courseMapper.selectCourse8Lid(id);
        if (!CollectionUtils.isEmpty(courseVos)) {
            return courseVos;
        }
        return null;
    }

    @Transactional
    @Override
    public String saveCouseInfo(CourseInfoDto info) {
        // 添加基本信息
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(info, course);
        int insert = courseMapper.insert(course);
        // 基本信息添加成功才能添加描述信息
        if (insert == 0) {
            throw new MyException(ResultEnum.COURSE_SAVE_ERROR);
        }
        // 添加描述信息

        CourseInfoDto desc = new CourseInfoDto();
        desc.setDescription(info.getDescription());
        desc.setId(course.getId());
        Boolean r = descriptionService.insertCourseDesc(desc);
        return r == true ? desc.getId() : null;

    }

    @Override
    public List a() {
        List<EduCourse> eduCourses = this.courseMapper.selectList(null);
        return eduCourses;
    }

    @Override
    public CourseInfoDto getCourseInfo(String id) {
        CourseInfoDto courseInfoDto = courseMapper.getCourseInfo(id);
        if (courseInfoDto == null) {
            throw new MyException(ResultEnum.NO_COURSE_INFO);
        }

        return courseInfoDto;
    }

    @Override
    public Boolean uploadCourse(CourseInfoDto info) {

        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(info, course);
        int i = this.courseMapper.updateById(course);
        if (i == 0) {
            throw new MyException(ResultEnum.FAIL_COURSE_UPLOAD);
        }
        EduCourseDescription description = new EduCourseDescription();
        description.setId(info.getId());
        description.setDescription(info.getDescription());
        Boolean r = descriptionService.uploadDesc(description);
        return r;
    }

    @Override
    public PageVo selectCondtion(Page p, CourseInfoDto info) {
        List lectIds = new ArrayList<>();
        String key = info.getKey();
        if (StringUtils.isNotBlank(key)) {
            info.setKey(MyString.convertColumn(info.getKey()));
        }
        Page page = this.courseMapper.selectCondtion(p, info);

        return new PageVo(page);
    }

    @Override
    public Boolean courseDelete(String id) {


        List<EduChapter> chapters = chapterMapper.queryListForChapter(id);
        if (!CollectionUtils.isEmpty(chapters)) {
            chapters.forEach(ch -> {
                String chid = ch.getId();
                // 查询章节中所有小节
                List<EduVideo> eduVideos = videoMapper.queryListForVideo(chid);
                eduVideos.forEach(video -> {
                    if (StringUtils.isNotEmpty(video.getVideoSourceId())) {
                        //删腾讯云上视频
                        cloudFeign.DeleteMedia(video.getVideoSourceId());
                    }
                    videoService.deleteVideo(video.getId());

                });

            });
            //删章节
            QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
            wrapper.eq("course_id", id);
            int delete = chapterMapper.delete(wrapper);

            return this.judge(id);

        } else {
            //不存在章节直接删描述
            return this.judge(id);
        }


    }

    @Override
    public CourseInfoDto queryInfo(String id) {
        CourseInfoDto infoDto = new CourseInfoDto();
        if (StringUtils.isEmpty(id)) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
       //  CourseInfoDto queryInfo = courseMapper.queryInfo(id);   xml
       // 课程信息
        EduCourse eduCourse = this.courseMapper.selectById(id);
        // 课程详情
        EduCourseDescription description = this.courseDescriptionMapper.selectById(id);
        // 讲师信息
        EduLecturer eduLecturer = this.lecturerMapper.selectOne(new LambdaQueryWrapper<EduLecturer>()
                .eq(EduLecturer::getId, eduCourse.getLecturerId()));
        // 课程1级类别
        EduSubject subjectOne = this.categoryMapper.selectOne(new LambdaQueryWrapper<EduSubject>()
                .eq(EduSubject::getId, eduCourse.getCategoryPid()));
        // 课程2级别
        EduSubject subjectTwo = this.categoryMapper.selectOne(new LambdaQueryWrapper<EduSubject>()
                .eq(EduSubject::getId, eduCourse.getCategoryId()));

        /**
         * 拼装数据
         */

         BeanUtils.copyProperties(eduCourse,infoDto);
         infoDto.setLecturerName(eduLecturer.getName()).setCareer(eduLecturer.getCareer())
                 .setIntro(eduLecturer.getIntro()).setHead(eduLecturer.getHead())
                 .setLevel(eduLecturer.getLevel()).setDescription(description.getDescription())
                 .setOneCategory(subjectOne.getTitle()).setTwoCategory(subjectTwo.getTitle());

         return infoDto;

    }

    @Override
    public Boolean statePut(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }

        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus(CourseConstant.COURSE_PUBLISH);

        int i = courseMapper.updateById(eduCourse);
        return i > 0;
    }

    @Override
    public Boolean changeStatus(String id, String status) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(status)) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        if (CourseConstant.COURSE_PUBLISH.equals(status)) {
            eduCourse.setStatus(CourseConstant.COURSE_LOWER);
        } else {
            eduCourse.setStatus(CourseConstant.COURSE_PUBLISH);
        }
        int i = courseMapper.updateById(eduCourse);
        return i > 0;
    }

    // web 获取课程信息
    @Override
    public Map getCourseList(Page<EduCourse> p, CourseVo bean) {
        Map<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<EduCourse> queryWrapper = new LambdaQueryWrapper<>();

        // 一级类别
        queryWrapper.eq(EduCourse::getStatus, CourseConstant.COURSE_PUBLISH);
        if ((StringUtils.isNotEmpty(bean.getCategoryPid()))) {
            queryWrapper.eq(EduCourse::getCategoryPid, bean.getCategoryPid());
         // 二级类别
         if (StringUtils.isNotEmpty(bean.getCategoryId())){
             queryWrapper.eq(EduCourse::getCategoryId,bean.getCategoryId());
         }
        }
        if ((StringUtils.isNotEmpty(bean.getSearch()))) {
            queryWrapper.likeRight(EduCourse::getTitle, bean.getSearch());
        }
     // 排序

        switch (bean.getSort()) {
            case 1:  // 免费课
                queryWrapper.orderByAsc(EduCourse::getPrice);

                break;
            case 2: // 收费课
                queryWrapper.orderByDesc(EduCourse::getPrice);
                break;
            case 3: // 热门
                queryWrapper.orderByDesc(EduCourse::getViewCount);
                break;
            case 4: // 新课
                queryWrapper.orderByDesc(EduCourse::getEduCreate);
                break;
            default:
                queryWrapper.orderByAsc(EduCourse::getEduCreate);
        }


        Page<EduCourse> coursePage = courseMapper.selectPage(p, queryWrapper);
        map.put("result", new PageVo(coursePage));

        return map;
    }

    private void getCategory(CourseVo bean, Map<String, Object> map) {
        EduSubject selectOne = this.categoryMapper.selectOne(new LambdaQueryWrapper<EduSubject>()
                .eq(EduSubject::getParentId, "0")
                .eq(EduSubject::getId, bean.getCategoryPid()));
        List<EduSubject> subjects = this.categoryMapper.selectList(new LambdaQueryWrapper<EduSubject>()
                .eq(EduSubject::getParentId, bean.getCategoryPid()));
        map.put("oneName", selectOne.getTitle());
        map.put("twoList", subjects);
    }

    @Override
    public Map getCourseDetails(String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        // 课程描述信息.讲师.分类
        CourseInfoDto courseInfo = courseService.queryInfo(id);
        // 记录访问次数
            Long viewCount = courseInfo.getViewCount();
            viewCount++;
            EduCourse eduCourse = new EduCourse().setId(id).setViewCount(viewCount);
            this.courseMapper.updateById(eduCourse);
        // 章节小节信息
        Map<String, List<ChapterDto>> listMap = chapterService.queryChapter(id);
        List<ChapterDto> all = listMap.get("all");
        hashMap.put("courseInfo", courseInfo);
        hashMap.put("chapterInfo", all);
        return hashMap;
    }

    @Override
    public Map queryCourseInfo(Page<EduCourse> returnPage, String id) {
        Map<String, Object> hashMap = new HashMap<>();
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("category_pid", id);
        wrapper.eq("status", "Publish");
        // 课程基本信息
        Page<EduCourse> coursePage = courseMapper.selectPage(returnPage, wrapper);
        hashMap.put("records", coursePage.getRecords());
        hashMap.put("total", coursePage.getTotal());
        hashMap.put("pages", coursePage.getPages());
        hashMap.put("size", coursePage.getSize());
        hashMap.put("current", coursePage.getCurrent());
        hashMap.put("previous", coursePage.hasPrevious());
        hashMap.put("next", coursePage.hasNext());
        // 一级列表名称
        EduSubject eduSubject = categoryMapper.selectById(id);
        hashMap.put("oneName", eduSubject.getTitle());
        // 二级列表集合
        QueryWrapper<EduSubject> categoryWrapper = new QueryWrapper<>();
        categoryWrapper.eq("parent_id", id);
        List<EduSubject> subjects = categoryMapper.selectList(categoryWrapper);
        hashMap.put("categoryList2", subjects);


        return hashMap;
    }

    @Override
    public Map queryCourseInfo1(Page<EduCourse> returnPage, String id) {
        Map<String, Object> hashMap = new HashMap<>();
        // 二级类别名称
        EduSubject eduSubject = categoryMapper.selectById(id);
        hashMap.put("twoName", eduSubject.getTitle());
        // 一级类别名称
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", "0");
        EduSubject subject = categoryMapper.selectById(eduSubject.getParentId());
        hashMap.put("oneName", subject.getTitle());
        hashMap.put("oneID", subject.getId());
        // 二级类别课程
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.eq("category_id", id);
        Page<EduCourse> coursePage = courseMapper.selectPage(returnPage, courseWrapper);
        hashMap.put("records", coursePage.getRecords());
        hashMap.put("total", coursePage.getTotal());
        hashMap.put("pages", coursePage.getPages());
        hashMap.put("size", coursePage.getSize());
        hashMap.put("current", coursePage.getCurrent());
        hashMap.put("previous", coursePage.hasPrevious());
        hashMap.put("next", coursePage.hasNext());


        return hashMap;
    }

    @Override
    @Cacheable(value = "index", key = "'course'")
    public List<EduCourse> queryPopularCourse() {
//        Page<EduCourse> page = new Page<>(1, 8);
//        Page<EduCourse> eduCoursePage = this.courseMapper.selectPage(page, new LambdaQueryWrapper<EduCourse>()
//                .eq(EduCourse::getStatus, CourseConstant.COURSE_PUBLISH)
//                .orderByAsc(EduCourse::getEduCreate));
//        if (CollectionUtils.isEmpty(eduCoursePage.getRecords())) {
//            return null;
//        }
       try {
           List<EduCourse> eduCourses = this.courseMapper.selectList(new LambdaQueryWrapper<EduCourse>()
                   .eq(EduCourse::getStatus, CourseConstant.COURSE_PUBLISH)
                   .orderByAsc(EduCourse::getEduCreate).last("limit 0,8"));
           return eduCourses;

       }catch (Exception e){
           e.printStackTrace();
           log.error("获取热门课程异常: {}", JSON.toJSONString(e.getMessage()));
       }
       return null;
    }

    @Override
    public VideoDto queryVideoInfo(String id) {
        VideoDto videoDto = videoMapper.queryVideoInfo(id);
        if (videoDto != null) {
            String videoSourceId = videoDto.getVideoSourceId();
            Future<String> videoAuth = this.asyncService.getVideoAuth(videoSourceId);
            Future<String> videoSource = this.asyncService.getVideoSource(videoSourceId);
            try {

                videoDto.setPlayAuth(videoAuth.get());
                System.out.println(videoAuth);
                System.out.println(videoAuth.get());
                videoDto.setSource(videoSource.get());

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        }


        return videoDto;
    }

    @Override
    public Integer userIsBuy(String cid, String token) {

//        UserBean in = this.authFeignClient.in(token);
//        if (in == null) {
//            return CourseConstant.NO_LOGIN_IN;
//        }
//        if (in.getId().equals(TokenConstant.TOKEN_OUT_CODE)) {
//            return CourseConstant.AUTH_ERROR;
//        }
//        String username = in.getUsername();
//
//        int i = this.courseMapper.userIsBuy(cid, username);
//
//
//        return i > 0 ? CourseConstant.BUY : CourseConstant.NOT_BUY;
        return 100;


//        AtomicReference<Boolean> result = new AtomicReference<>(false);
//          // 查询redis是否有购买数据 redis 插入id有问题
//        List<String> list = this.redisTemplate.opsForList().range(username, 0, -1);
//            if (list.size() == 0) {
//                return result.get();
//            }else {
//                list.forEach(s -> {
//                    if (cid.equals(s)) {
//                        result.set(true);
//                    }else {
//                        result.set(false);
//                    }
//                });
//            }
//
//        return result.get();
    }

    @Override
    public CourseVo getOrderInfo(String cid) {
        EduCourse eduCourse = this.courseMapper.selectById(cid);
        if (eduCourse == null) {
            throw new MyException(ResultEnum.QUERY_ERROR);
        }
        CourseVo courseVo = new CourseVo();
        BeanUtils.copyProperties(eduCourse, courseVo);
        return courseVo;
    }

    @Override
    public Boolean addUcourse(String courseId, String username) {
        HashMap<String, Object> hashMap = new HashMap<>();
        long l = SnowFlake.nextId();
        String id = String.valueOf(l);
        System.out.println(id);
        hashMap.put("id", id);
        hashMap.put("courseId", courseId);
        hashMap.put("username", username);
        int i = this.courseMapper.addUcourse(hashMap);

        return i > 0 ? true : false;
    }

    @Override
    public PageVo getMyCourse(Page page, String username) {

        Page myCourse = this.courseMapper.selectMyCourse(page, username);
        return new PageVo(myCourse);


    }

    @Override
    public Integer queryCourse(String day) {

        Integer integer = this.courseMapper.queryCourse(day);
        return integer;
    }

    @Override
    public List<TwoSubjectDto> getSecondListByFirsrId(String firstId) {
        List<TwoSubjectDto> list = new ArrayList<>();
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", firstId);
        List<EduSubject> category = this.categoryMapper.selectList(wrapper);
        category.forEach(x -> {
            TwoSubjectDto two = new TwoSubjectDto();
            two.setId(x.getId());
            two.setTitle(x.getTitle());
            list.add(two);

        });

        return list;
    }

    @Override
    public CourseVo getcourse(String cid) {
        CourseVo courseVo = this.courseMapper.selectBycourseL(cid);

        return courseVo;
    }

    @Override
    public Integer record(String cid, UserBean jwt) {
        String username = "";
         if (StringUtils.isNoneBlank(jwt.getUsername())){
             username = jwt.getUsername();
         }

        Integer reID = this.courseMapper.findCid(cid,username );

         // 有重复记录
         if (reID != null){
             this.courseMapper.deleteStudyId(cid,username);
         }

        if (reID == 0 || reID == null) {
            long snow = SnowFlake.nextId();
            String id = String.valueOf(snow);
            Integer record = this.courseMapper.record(id, cid, username);
            return record;
        }
        return 0;

    }

    @Override
    @Transactional
    public void delete(String[] ids) {

        for (int i = 0; i < ids.length; i++) {
            Boolean aBoolean = courseDelete(ids[i]);
            if (!aBoolean) {
                throw new MyException(ResultEnum.DELETE_ERROR);
            }
        }

    }

    @Override
    public String queryForLect(String cid) {
        EduCourse eduCourse = this.courseMapper.selectById(cid);
        if (eduCourse != null) {
            return eduCourse.getLecturerId();
        }

        return null;
    }

    @Override
    public CourseBean orderquery(String courseId) {
        CourseBean eduCourse = this.courseMapper.orderquery(courseId);


        return eduCourse;
    }

    @Override
    public Long statisticsForCourse(String day) {

        AtomicReference<Long> res = new AtomicReference<>(0L);
        LocalDateTime startTime = LocalDateTimeUtils.getStartTime(day);
        LocalDateTime endTime = LocalDateTimeUtils.getEndTime(day);
        List<EduCourse> eduCourses = this.courseMapper.selectList(new LambdaQueryWrapper<EduCourse>()
                .ge(EduCourse::getEduModified, startTime)
                .le(EduCourse::getEduModified, endTime));
        eduCourses.forEach((course) -> {
            Long buyCount = course.getBuyCount();
            res.updateAndGet(v -> v + buyCount);
        });

        Long aLong = res.get();


        return aLong;
    }

    @Override
    public Integer queryStudy(String username, String id) {

        return   this.courseMapper.findCid(username, id);


    }

    @Override
    public Integer bought(String courseid, String username) {

        Integer count = this.courseMapper.findCid(username, courseid);
        if (count != 0){
            this.courseMapper.deleteStudyId(courseid,username);
        }

        return this.courseMapper.record(IdWorker.getTimeId(), courseid, username);
    }


    //删除描述和课程
    public boolean judge(String param) {

        int i = descriptionMapper.deleteById(param);

        if (i > 0) {
            //删课程
            return courseMapper.deleteById(param) > 0;

        } else {
            log.error("删除课程描述失败: 课程idID[{}]", param);
            throw new MyException(ResultEnum.DELETE_ERROR);
        }


    }

}
