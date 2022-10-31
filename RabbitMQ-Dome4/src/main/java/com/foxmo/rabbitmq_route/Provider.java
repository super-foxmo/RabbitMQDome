package com.foxmo.rabbitmq_route;

import com.foxmo.rabbitmq_route.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

//路由模式
public class Provider {
    public static void main(String[] args) throws IOException {
        //获取连接通道
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        //通过通道声明交换机
        channel.exchangeDeclare("logs_direct","direct");
        //发送消息
        String routingKey = "info";
        channel.basicPublish("logs_direct",routingKey,null,("这是direct模型发布的基于route key：" + routingKey).getBytes());
        //释放资源
        RabbitMQUtils.closeConnectionAndChanel(channel,connection);
    }
}
