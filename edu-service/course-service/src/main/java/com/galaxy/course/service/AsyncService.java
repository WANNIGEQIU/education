package com.galaxy.course.service;

import com.galaxy.common.util.ResultCommon;
import com.galaxy.course.entity.EduCourse;
import com.galaxy.course.feign.CloudFeign;
import com.galaxy.course.mapper.EduCourseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class AsyncService {

   private Lock lock = new ReentrantLock();


    @Autowired
    private CloudFeign cloudFeign;

    @Autowired
    private EduCourseMapper courseMapper;

    @Async
    public Future<String> getVideoAuth(String videoSourceId){
        log.info("获取上传凭证: {}",Thread.currentThread().getName()+"  "+videoSourceId);

        ResultCommon videoAuth = this.cloudFeign.getVideoAuth(videoSourceId);
        String source = (String) videoAuth.getData();
        return new AsyncResult<>(source);

    }

    @Async
    public Future<String> getVideoSource(String videoSourceId){
        log.info("获取视频地址: {}",Thread.currentThread().getName()+" "+videoSourceId);
        ResultCommon videoPlay = cloudFeign.getVideoPlay(videoSourceId);
        String play = (String) videoPlay.getData();

        return new AsyncResult<>(play);


    }

    @Async
    public void addCourseView(Long count,String id){

        try {
            System.out.println(Thread.currentThread().getName()+"-before-"+count);
            count++;
            EduCourse eduCourse = new EduCourse();
            EduCourse course = eduCourse.setId(id).setViewCount(count);
             this.courseMapper.updateById(course);
            System.out.println(Thread.currentThread().getName()+"-end-"+count);


        } finally {
        }

    }



}
