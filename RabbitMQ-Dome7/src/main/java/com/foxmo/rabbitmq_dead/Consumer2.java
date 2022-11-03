package com.foxmo.rabbitmq_dead;

import com.foxmo.rabbitmq_dead.utils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * 普通消息转化成死信的原因：
 * 1、消息被拒绝
 * 2、消息的TTL过期
 * 3、队列达到最大长度（无法在接收生产者生产的消息）
 */

public class Consumer2 {
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException {
        //获取通道连接
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();

        //接收消息成功 回调方法
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer2接收的消息" + new String(message.getBody(),"UTF-8"));
        };

        //接收消息失败 回调方法
        CancelCallback cancelCallback = (consumerTag) -> {

        };

        //消费消息
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,cancelCallback);
    }
}
