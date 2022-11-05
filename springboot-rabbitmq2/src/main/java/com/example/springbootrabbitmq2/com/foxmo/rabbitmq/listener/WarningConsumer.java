package com.example.springbootrabbitmq2.com.foxmo.rabbitmq.listener;

import com.example.springbootrabbitmq2.com.foxmo.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarningConsumer {
    //接收消息
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message){
        log.info("报警发现不可路由消息：{}",new String(message.getBody()));
    }
}
