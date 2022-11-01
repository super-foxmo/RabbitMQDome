package com.foxmo.rabbitmq.fanout;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class FanoutConsumer {
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue, //若没有指定名称，则创建临时队列
                    exchange = @Exchange(value = "error",type = "fanout")
            )
    })
    public void receive1(String message){
        System.out.println("*********************消费者1：" + message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue, //若没有指定名称，则创建临时队列
                    exchange = @Exchange(value = "error",type = "fanout")
            )
    })
    public void receive2(String message){
        System.out.println("*********************消费者2：" + message);
    }
}
