package com.foxmo.rabbitmq_topic;

import com.foxmo.rabbitmq_topic.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;


//topic动态路由模型
public class Provider {
    public static void main(String[] args) throws IOException {
        //获取通道连接
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        //通过通道声明交换机
        channel.exchangeDeclare("topics","topic");
        //发布消息
        String routingKey = "user.save";
        channel.basicPublish("topics",routingKey,null,("这是topic动态路由模型，routingKey：[" + routingKey + "]").getBytes());
        //释放资源
        RabbitMQUtils.closeConnectionAndChanel(channel,connection);
    }
}
