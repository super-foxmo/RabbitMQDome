package com.foxmo.rabbitmq.work;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkConsumer {
    @RabbitListener(queuesToDeclare = @Queue(value = "work",exclusive = "false",durable = "false",autoDelete = "true"))
    public void receive1(String message){
        System.out.println("**********************消费者1：" + message);
    }

    @RabbitListener(queuesToDeclare = @Queue(value = "work",exclusive = "false",durable = "false",autoDelete = "true"))
    public void receive2(String message){
        System.out.println("**********************消费者2：" + message);
    }
}

