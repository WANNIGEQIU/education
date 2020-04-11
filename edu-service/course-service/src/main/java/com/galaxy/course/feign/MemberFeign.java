package com.galaxy.course.feign;

import com.galaxy.base.memberapi.MemberApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("edu-member")
public interface MemberFeign  extends MemberApi {
}
