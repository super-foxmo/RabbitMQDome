package com.example.springbootrabbitmq2.com.foxmo.rabbitmq.controller;

import com.example.springbootrabbitmq2.com.foxmo.rabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable("message") String message) {
        log.info("当前时间：{}，发送消息：{}",new Date().toString(),message);
        //发送消息
//        channel.basicPublish(NORMAL_EXCHANGE,"a_normal_key",null,message.getBytes());
        rabbitTemplate.convertAndSend(NORMAL_EXCHANGE,"b_normal_key",("延迟30秒的消息：" + message).getBytes(StandardCharsets.UTF_8));
        rabbitTemplate.convertAndSend(NORMAL_EXCHANGE,"a_normal_key",("延迟10秒的消息：" + message).getBytes(StandardCharsets.UTF_8));

    }

    /**
     * 自定义延迟时间消息
     * 由于队列的先进先出特性，只有当过期的消息到了队列的顶端（队首），才会被真正的丢弃或者进入死信队列。
     * 所以在考虑使用RabbitMQ来实现延迟任务队列的时候，需要确保业务上每个任务的延迟时间是一致的。
     * 如果遇到不同的任务类型需要不同的延时的话，需要为每一种不同延迟时间的消息建立单独的消息队列。
     * @param message   发送消息
     * @param ttlTime   延迟时长
     */
    @GetMapping("/sendOutTimeMessage/{message}/{ttlTime}")
    public void sendOutTimeMessage(@PathVariable("message") String message,
                                   @PathVariable("ttlTime") String ttlTime) {

        log.info("当前时间：{}，发送一条延迟时长为{}的消息：{}",new Date().toString(),ttlTime,message);
        //发送消息
        rabbitTemplate.convertAndSend(NORMAL_EXCHANGE,"c_normal_key",("自定义延迟消息：" + message).getBytes(StandardCharsets.UTF_8),msg ->{
            //设置延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });

    }

    @GetMapping("/sendDelayedMessage/{message}/{ttlTime}")
    public void sendDelayedMessage(@PathVariable("message") String message,
                                   @PathVariable("ttlTime") Integer ttlTime) {
        log.info("当前时间：{}，发送一条延迟时长为{}的消息：{}",new Date().toString(),ttlTime,message);
        //发送消息
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,DelayedQueueConfig.DELAYED_ROUTING_KEY,("自定义延迟消息：" + message).getBytes(StandardCharsets.UTF_8), msg ->{
            //设置延迟时长
            msg.getMessageProperties().setDelay(ttlTime);
            return msg;
        });
    }


}
