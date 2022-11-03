package com.foxmo.rabbitmq_confirm;

import com.foxmo.rabbitmq_confirm.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmMessage {
    public static void main(String[] args) throws IOException, InterruptedException {
        //1、单个确认
        publishMessageIndividually();
        //2、批量确认
        publishMessageBatch();
        //3、异步批量确认
        publishMessageAsync();

    }

    //单个消息确认
    public static void publishMessageIndividually() throws IOException, InterruptedException {
        //获取连接通道
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        //开启发布确认
        channel.confirmSelect();
        //绑定队列
        channel.queueDeclare("confirm",true,false,true,null);

        //开始时间
        long begin = System.currentTimeMillis();
        //发布消息
        for (int i = 0; i < 100; i++) {
            String message = new String("单个消息确认" + i);
            channel.basicPublish("","confirm", MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            //单个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
//            if (flag){
//                System.out.println("消息发送成功");
//            }
        }
        //结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布100条单个确认消息，耗时：" + (end - begin));
    }

    //2、批量确认
    public static void publishMessageBatch() throws IOException, InterruptedException {
        //获取连接通道
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        //开启发布确认
        channel.confirmSelect();
        //绑定队列
        channel.queueDeclare("confirm",true,false,true,null);

        //开始时间
        long begin = System.currentTimeMillis();
        //发布消息
        for (int i = 1; i <= 100; i++) {
            String message = new String("批量消息确认" + i);
            channel.basicPublish("","confirm", MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            if (i % 10 == 0){

                boolean flag = channel.waitForConfirms();
//                if (flag){
//                    System.out.println("消息发送成功");
//                }
            }
        }
        //结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布100条批量确认消息，耗时：" + (end - begin));
    }

    //3、异步批量确认
    public static void publishMessageAsync() throws IOException, InterruptedException {
        //获取连接通道
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        //开启发布确认
        channel.confirmSelect();
        //绑定队列
        channel.queueDeclare("confirm",true,false,true,null);

        //线程安全有序的一个哈希表，使用于高并发的情况下
        ConcurrentSkipListMap<Long, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();

        //消息确认成功 回调方法
        ConfirmCallback ackCallback = (deliveryTag,multiple) -> {
            //删除掉已经确认的信息，剩下的就是未确认的
            if (multiple){  //批量确认
                ConcurrentNavigableMap<Long, String> confirmed = concurrentSkipListMap.headMap(deliveryTag);
                confirmed.clear();
            }else{  //单个确认
                concurrentSkipListMap.remove(deliveryTag);
            }
            System.out.println("确认消息：" + deliveryTag);
        };

        //消息确认失败 回调方法
        ConfirmCallback nackCallback = (deliveryTag,multiple) -> {
            //获取未确认的消息
            String message = concurrentSkipListMap.get(deliveryTag);
            System.out.println("未确认的消息：" + message + " *********** 未确认的消息tag：" + deliveryTag);
        };

        //消息的确认监听器
        channel.addConfirmListener(ackCallback,nackCallback);

        //开始时间
        long begin = System.currentTimeMillis();
        //发布消息
        for (int i = 1; i <= 100; i++) {
            String message = new String("异步批量消息确认" + i);
            channel.basicPublish("","confirm", MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(),message);
        }

        //结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布100条异步批量确认消息，耗时：" + (end - begin));

        System.out.println(concurrentSkipListMap.toString());
        System.out.println("*******************************");
        Thread.sleep(10000);

        System.out.println(concurrentSkipListMap.toString());
        System.out.println("*******************************");
    }
}
