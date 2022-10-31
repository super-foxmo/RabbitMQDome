package com.foxmo.rabbitmq_route;

import com.foxmo.rabbitmq_route.utils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer2 {
    public static void main(String[] args) throws IOException {
        //获取连接通道
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        //通过通道声明交换机
        channel.exchangeDeclare("logs_direct","direct");
        //创建临时队列
        String queue = channel.queueDeclare().getQueue();
        //基于direct key绑定队列与交换机
        String routingKey1 = "info";
        String routingKey2 = "error";
        String routingKey3 = "log";

        channel.queueBind(queue,"logs_direct",routingKey1);
        channel.queueBind(queue,"logs_direct",routingKey2);
        channel.queueBind(queue,"logs_direct",routingKey3);
        //消费消息
        channel.basicConsume(queue,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者2：" + new String(body));
            }
        });
    }
}
