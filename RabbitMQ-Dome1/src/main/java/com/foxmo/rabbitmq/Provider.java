package com.foxmo.rabbitmq;

import com.foxmo.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import org.junit.Test;

import java.io.IOException;

//直连模型（一对一）
public class Provider {
    @Test
    public void testSendMessage() throws IOException {
//        //创建连接mq的连接工厂对象
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        //设置连接rabbitmq的主机
//        connectionFactory.setHost("192.168.250.134");
//        //设置端口
//        connectionFactory.setPort(5672);
//        //设置连接的虚拟主机
//        connectionFactory.setVirtualHost("/foxmo");
//        //设置访问虚拟主机的用户名和密码
//        connectionFactory.setUsername("foxmo");
//        connectionFactory.setPassword("foxmo");
//
//        //获取连接对象
//        Connection connection = connectionFactory.newConnection();

        Connection connection = RabbitMQUtils.getConnection();

        //创建连接通道
        Channel channel = connection.createChannel();

        //通道绑定到对应的消息队列
        /**
         * 参数1：队列名称，如果该队列不存在将会自动创建
         * 参数2：定义队列特性是否要持久化，true：持久化队列      false：不持久化队列
         * 参数3：是否独占队列  true：独占队列        false：不独占队列
         * 参数4：是否在消费完成后自动删除队列   true：自动删除        false：不自动删除
         * 参数5：额外附加参数
         *
         * 注意： 生产者和消费者绑定的队列的属性必须一致
         */
        channel.queueDeclare("hello",false,false,false,null);

        //发布消息
        /**
         * 参数1：交换机名称
         * 参数2：队列名称
         * 参数3：传输消息额外设置     MessageProperties.PERSISTENT_TEXT_PLAIN ---> 持久化消息
         * 参数4：消息的具体内容
         */
        channel.basicPublish("","hello", MessageProperties.PERSISTENT_TEXT_PLAIN,"hello rabbitmq".getBytes());

//        //关闭资源
//        channel.close();
//        connection.close();
        RabbitMQUtils.closeConnectionAndChanel(channel,connection);
    }
}
