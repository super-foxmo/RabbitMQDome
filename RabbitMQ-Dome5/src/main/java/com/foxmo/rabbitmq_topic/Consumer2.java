package com.foxmo.rabbitmq_topic;

import com.foxmo.rabbitmq_topic.utils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer2 {
    public static void main(String[] args) throws IOException {
        //获取连接通道
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        //通过通道声明交换机
        channel.exchangeDeclare("topics","topic");
        //创建临时队列
        String queue = channel.queueDeclare().getQueue();
        //绑定交换机与队列
        String routingKey = "user.#.*";
        channel.queueBind(queue,"topics",routingKey);
        //消费消息
        channel.basicConsume(queue,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者2：" + new String(body));
            }
        });
    }
}
