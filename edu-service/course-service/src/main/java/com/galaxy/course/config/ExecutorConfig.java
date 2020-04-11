package com.galaxy.course.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;


@Configuration
@EnableAsync
@Slf4j
public class ExecutorConfig {


             @Value("${async.corePoolSize}")
             private int corePoolSize;

             @Value("${async.maxPoolSize}")
             private int maxPoolSize;

             @Value("${async.queue}")
             private int queueCapacity;

             @Value("${async.threadName}")
             private String threadPrefix;

             @Bean
             public TaskExecutor asyncServiceExecutor() {
                 ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
                 taskExecutor.setCorePoolSize(corePoolSize);  //Runtime.getRuntime().availableProcessors()+1
                 taskExecutor.setMaxPoolSize(maxPoolSize);
                 taskExecutor.setKeepAliveSeconds(10);
                 taskExecutor.setQueueCapacity(queueCapacity);
                 taskExecutor.setThreadNamePrefix(threadPrefix);
                 taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
                 taskExecutor.initialize();
                 return taskExecutor;

             }


}
