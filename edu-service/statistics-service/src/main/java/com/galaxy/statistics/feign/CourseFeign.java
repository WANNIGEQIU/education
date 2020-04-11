package com.galaxy.statistics.feign;

import com.galaxy.base.courseapi.CourseApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;


@Component
@FeignClient("edu-course")
public interface CourseFeign extends CourseApi {
}
