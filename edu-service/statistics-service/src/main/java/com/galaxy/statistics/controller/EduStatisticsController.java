package com.galaxy.statistics.controller;


import com.galaxy.statistics.service.EduStatisticsService;
import com.galaxy.common.util.ResultCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-21
 */
@RestController
@RequestMapping("/statistics")
public class EduStatisticsController {
    @Autowired
    private EduStatisticsService statisticsService;


    /**
     * 刷新or生成 统计记录
     * @param day
     * @return
     */
    @GetMapping("/dayrecord/{day}")
    public ResultCommon dayRecord(@PathVariable("day") String day) {
        int i = statisticsService.dayRecord(day);
        if (i>0) {
            return ResultCommon.resultOk(i);
        }else {
            return ResultCommon.resultFail().data(i);
        }
    }

    /**
     * 查询统计
     * @param type
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/showchart/{type}/{beginTime}/{endTime}")
    public ResultCommon showChart(@PathVariable String type,
                                  @PathVariable String beginTime,
                                  @PathVariable String endTime) {
        Map map = statisticsService.showChart(type, beginTime, endTime);
        return ResultCommon.resultOk(map);
    }



}

