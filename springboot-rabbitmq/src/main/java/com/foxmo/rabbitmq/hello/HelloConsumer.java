package com.foxmo.rabbitmq.hello;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component      //默认未：持久化，非独占，非自动删除队列
@RabbitListener(queuesToDeclare = @Queue(value = "hello",durable = "false",autoDelete = "true",exclusive = "false"))  //queuesToDeclare:若队列不存在，则创建该队列
public class HelloConsumer {
    //接收带队列的消息的回调方法
    @RabbitHandler
    public void receive(String message){
        System.out.println("message = " + message);
    }
}
