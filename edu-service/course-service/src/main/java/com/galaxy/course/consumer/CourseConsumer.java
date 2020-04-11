package com.galaxy.course.consumer;

import com.galaxy.course.entity.EduVideo;
import com.galaxy.common.bean.BaseVideoDto;
import com.galaxy.course.entity.EduCourse;
import com.galaxy.course.mapper.EduCourseMapper;
import com.galaxy.course.mapper.EduVideoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CourseConsumer {

    @Autowired
    private EduCourseMapper courseMapper;

    @Autowired
    private EduVideoMapper videoMapper;



    /**
     * 消费订单支付成功后的消息
     *
     * @param courseId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "H-ORDER", durable = "true"),
            exchange = @Exchange(value = "ORDER", ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"order"}

    ))
    public void sendMessage(String courseId) {
        log.info("mq消费的id：{}", courseId);
        if (StringUtils.isEmpty(courseId)) {
            return;
        }
        EduCourse eduCourse = this.courseMapper.selectById(courseId);
        eduCourse.setBuyCount(eduCourse.getBuyCount() + 1L);
        int i = this.courseMapper.updateById(eduCourse);
        log.info("order-mq 更新课程购买数量影响行数: {}", i);


    }

    /**
     * 消费视频上传消息
     *
     * @param videoDto 视频凭证
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "H-CLOUD", durable = "true"),
            exchange = @Exchange(value = "VIDEO", ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"video"}
    ))
    public void sendVideoMsg(BaseVideoDto videoDto) {

        try {
            log.info("课程mq消费: {}", videoDto);
            EduVideo eduVideo = new EduVideo()
                    .setId(videoDto.getId()).setVideoSourceId(videoDto.getVideoId())
                    .setVideoName(videoDto.getTitle())
                    .setSize(Long.valueOf(videoDto.getSize())).setDuration(Double.valueOf(videoDto.getDuration()));
            int i = this.videoMapper.updateById(eduVideo);
            log.info("VIDEO-MQ 影响行数: {}", i);

        } catch (Exception e){

            log.info("视频消费失败: {}",videoDto);
        }


    }


}
