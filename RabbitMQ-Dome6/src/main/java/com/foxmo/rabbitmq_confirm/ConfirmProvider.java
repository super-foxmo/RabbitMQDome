package com.foxmo.rabbitmq_confirm;

import com.foxmo.rabbitmq_confirm.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class ConfirmProvider {
    public static void main(String[] args) throws IOException, InterruptedException {
        //获取连接通道
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        //开启发布确认
        channel.confirmSelect();
        //绑定队列
        channel.queueDeclare("confirm",true,false,true,null);
        //发布消息
//        Scanner scanner = new Scanner(System.in);
//        while(scanner.hasNext()){
//            String message = scanner.next();
//
//            channel.basicPublish("","confirm", MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
//            //单个消息就马上进行发布确认
//            boolean flag = channel.waitForConfirms();
//            if (flag){
//                System.out.println("消息发送成功");
//            }
//        }

        //开始时间
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            String message = new String("单个消息确认" + i);
            channel.basicPublish("","confirm", MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            //单个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag){
                System.out.println("消息发送成功");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布100条单个确认消息，耗时：" + (end - begin));
    }
}
