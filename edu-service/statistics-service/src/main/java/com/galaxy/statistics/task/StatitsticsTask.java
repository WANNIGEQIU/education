package com.galaxy.statistics.task;

import com.galaxy.statistics.service.EduStatisticsService;
import com.galaxy.common.util.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class StatitsticsTask {

    @Autowired
    private EduStatisticsService eduStatisticsService;

    /**
     * 每天执行
     */
    @Scheduled(cron = "0 0 0 1/1 * ?")
    //@Scheduled(cron = "*/3 * * * * ?")   // 3s间隔
    private void configureTasks() {
        String s = LocalDateTimeUtils.formatOther("yyyy-MM-dd");
        eduStatisticsService.dayRecord(s);

        System.out.println("我是一个定时任务");
    }

}
