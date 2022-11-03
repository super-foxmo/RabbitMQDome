package com.example.springbootrabbitmq2.com.foxmo.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class DeadQueueConsumer {
    private static final String DEAD_QUEUE = "dead_queue";

    //消费消息
    @RabbitListener(queues = DEAD_QUEUE)
    public void receive1(Message message, Channel channel){
        byte[] messageBody = message.getBody();

        log.info("当前时间：{}，接收到的死信消息：{}",new Date().toString(),new String(messageBody));
    }
}
