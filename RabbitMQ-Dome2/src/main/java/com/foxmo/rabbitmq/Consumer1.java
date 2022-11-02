package com.foxmo.rabbitmq;

import com.foxmo.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer1 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        //设置通道每次只能消费一个消息
        //这就相当于设置consumer的信道容量，0就是无限大，1就是1，2就是2，但是分发方式还是轮询，只不过容量满了就会跳过
        channel.basicQos(1);

        channel.queueDeclare("work",false,false,false,null);

        channel.basicConsume("work",false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("consumer1 ---> " + new String(body));

                //手动消息确认
                /**
                 * 参数1：手动确认消息标识
                 * 参数2：是否开启多个消息同时确认（false 每次确认一个）
                 */
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });
    }
}
