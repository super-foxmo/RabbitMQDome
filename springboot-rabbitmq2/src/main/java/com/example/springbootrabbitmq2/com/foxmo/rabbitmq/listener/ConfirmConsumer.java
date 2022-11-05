package com.example.springbootrabbitmq2.com.foxmo.rabbitmq.listener;

import com.example.springbootrabbitmq2.com.foxmo.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfirmConsumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receive(Message message){
        log.info("接收到的消息：" + new String(message.getBody()));
    }
}
