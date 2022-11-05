package com.example.springbootrabbitmq2.com.foxmo.rabbitmq.controller;

import com.example.springbootrabbitmq2.com.foxmo.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class ProviderController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable("message") String message){
        CorrelationData correlationData = new CorrelationData("1");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME + "1",ConfirmConfig.CONFIRM_ROUTING_KEY,
                message,correlationData);

        log.info("发送的消息为：{}",message);
    }
}
