package com.foxmo.rabbitmq;

import com.foxmo.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException {
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setHost("192.168.250.134");
//        connectionFactory.setPort(5672);
//        connectionFactory.setVirtualHost("/foxmo");
//        connectionFactory.setUsername("foxmo");
//        connectionFactory.setPassword("foxmo");
//
//        Connection connection = connectionFactory.newConnection();

        Connection connection = RabbitMQUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare("hello",false,false,false,null);

        /**
         * 参数1：队列名称
         * 参数2：消息自动确认机制（默认值：true  消费者自动向rabbitmq确认消息消费）
         */
        channel.basicConsume("hello",true,new DefaultConsumer(channel){
            //body: 消费者从消息队列中取出的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("******************" + new String(body));
            }
        });
    }
}
