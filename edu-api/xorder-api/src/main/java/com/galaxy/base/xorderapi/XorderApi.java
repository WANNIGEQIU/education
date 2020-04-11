package com.galaxy.base.xorderapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface XorderApi {

    @GetMapping("/order/queryOrderNum/{day}")
    Integer queryOrderNum(@PathVariable("day") String day);
}
