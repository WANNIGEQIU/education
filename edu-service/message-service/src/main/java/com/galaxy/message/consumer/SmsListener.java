package com.galaxy.message.consumer;



import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.message.utils.SendSms;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
@Slf4j
public class SmsListener {


    /**
     * 注册发短信
     * @param map
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "smsQueue",durable = "true"),
           exchange = @Exchange(value = "heimaoSms",ignoreDeclarationExceptions = "true",
           type = ExchangeTypes.TOPIC),
            key = {"sms"}
    ))

    public void sendMessage(Map<String,String> map) {
        judgeMap(map);

    }

    /**
     * 忘记密码发短信
     */

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LOSS",durable = "true"),
            exchange = @Exchange(value = "loss",ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"loss"}
    ))
    public void lossPassword(Map<String,String> map) {
        judgeMap(map);

    }




    private void judgeMap(Map<String, String> map) {
        if (CollectionUtils.isEmpty(map))
            return;
        String mobile = map.get("mobile");
        String verifyCode = map.get("verifyCode");
        log.info("短信服务 ---> 消费: {},{}",mobile,verifyCode);
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(verifyCode)) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }
        SendSms.sendSms(mobile, verifyCode);
    }


}



