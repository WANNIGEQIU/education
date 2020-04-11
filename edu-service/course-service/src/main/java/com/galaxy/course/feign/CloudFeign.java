package com.galaxy.course.feign;

import com.galaxy.base.cloudapi.CloudApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("edu-cloud")
public interface CloudFeign extends CloudApi {




}
