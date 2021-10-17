package com.yangbo.seckill.seckill.rabbitmq;

import com.yangbo.seckill.seckill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
    public static Logger log = LoggerFactory.getLogger(MQReceiver.class);
    @Autowired
    AmqpTemplate amqpTemplate;

    public void seckillMessage(SeckillMessage message) {
        String msg = RedisService.beanToString(message);
        log.info("秒杀生产者发送消息：send"+msg);
        //发送消息
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE,msg);
    }

    /**
    public void send(Object message){
        String msg = RedisService.beanToString(message);
        log.info("direct模式：send"+msg);
        //发送消息
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
    }

    public void sendTopic(Object message){
        String msg = RedisService.beanToString(message);
        log.info("topic模式 send "+msg);
        //发送消息   通过交换机发送/将要匹配的队列/消息内容
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key1",msg+"1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",msg+"2");
    }

    public void sendFanout(Object message){
        String msg = RedisService.beanToString(message);
        log.info("fanout模式 send "+msg);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg+"1");
    }

    public void sendHeader(Object message){
        String msg = RedisService.beanToString(message);
        log.info("header模式 send "+msg);
        //设置头部  匹配
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1","value1");
        properties.setHeader("header2","value2");
        //发送的消息
        Message obj = new Message(msg.getBytes(),properties);
        amqpTemplate.convertAndSend(MQConfig.HEADER_EXCHANGE,"",obj);
    }
    **/


}
