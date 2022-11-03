package com.foxmo.rabbitmq_dead;

import com.foxmo.rabbitmq_dead.utils.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public class Provider {
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException {
        //获取连接通道
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        //设置过期时间(10s)
//        AMQP.BasicProperties properties =
//                new AMQP.BasicProperties()
//                .builder().expiration("10000").build();
        //发送消息
        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE,"normal_key",null,message.getBytes("UTF-8"));
        }

        //释放资源
        RabbitMQUtils.closeConnectionAndChanel(channel,connection);
    }
}
