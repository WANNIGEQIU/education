package com.galaxy.statistics.feign;


import com.galaxy.base.xorderapi.XorderApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("edu-order")
public interface OrderFeign extends XorderApi {
}
