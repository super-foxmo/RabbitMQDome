package com.foxmo.rabbitmq_dead;

import com.foxmo.rabbitmq_dead.utils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 普通消息转化成死信的原因：
 * 1、消息被拒绝
 * 2、消息的TTL过期
 * 3、队列达到最大长度（无法再接收生产者生产的消息）
 */

public class Consumer1 {
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    private static final String DEAD_EXCHANGE = "dead_exchange";

    private static final String NORMAL_QUEUE = "normal_queue";

    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException {
        //获取通道连接
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        //声明普通和死信交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);
        //声明普通和死信队列
        HashMap<String, Object> arguments = new HashMap<>();
//        //过期时间 10s    也可以在provider端设置过期时间
//        arguments.put("x-message-ttl",10000);
        //当普通队列中的普通消息变成死信时，将要传输到的死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信交换机于普通队列之间的routingKey
        arguments.put("x-dead-letter-routing-key","dead_key");
        //设置正常队列长度的限制
        arguments.put("x-max-length",6);

        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);

        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
        //绑定普通交换机与普通队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"normal_key");
        //绑定死信交换机与死信队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"dead_key");

        //接收消息成功 回调方法
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), "UTF-8");
            if (msg.equals("info5")){
                System.out.println("Consumer1拒绝接收的消息" + msg);
                //参数2：true -> 将消息重新放回原来的队列，等待被其他消费者消费    false -> 不将消息重新放回原来的队列，该消息将成为死信
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else{
                System.out.println("Consumer1接收的消息" + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };

        //接收消息失败 回调方法
        CancelCallback cancelCallback = (consumerTag) -> {

        };

        //消费消息(开启手动确认)
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,cancelCallback);

    }
}
