package com.yangbo.seckill.seckill.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    public static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    /**
     * direct模式 消息接收者
     * @param message
     */
    @RabbitListener(queues = MQConfig.QUEUE)
    public void receiver(String message){
        log.info("direct模式 receive message："+message);
    }

    /**
     * topic模式  消息接收者
     */
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void topicReceiver1(String message){
        log.info("topic模式1 receive message："+message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void topicReceiver2(String message){
        log.info("topic模式2 receive message："+message);
    }



}
