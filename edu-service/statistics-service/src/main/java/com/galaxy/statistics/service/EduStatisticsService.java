package com.galaxy.statistics.service;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-21
 */
public interface EduStatisticsService  {

    int dayRecord(String day);

    Map showChart(String type, String beginTime, String endTime);
}
