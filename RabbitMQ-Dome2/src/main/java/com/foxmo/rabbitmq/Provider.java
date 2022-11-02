package com.foxmo.rabbitmq;

import com.foxmo.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

 public class Provider {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare("work",false,false,false,null);

        for (int i = 0; i < 100; i++) {
            channel.basicPublish("","work",null,("work queue --> " + i).getBytes());
        }

        RabbitMQUtils.closeConnectionAndChanel(channel,connection);
    }
}
