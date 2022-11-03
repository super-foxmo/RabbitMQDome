package com.example.springbootrabbitmq2.com.foxmo.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class TTLQueueConfig {
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    private static final String DEAD_EXCHANGE = "dead_exchange";

    private static final String A_NORMAL_QUEUE = "a_normal_queue";

    private static final String B_NORMAL_QUEUE = "b_normal_queue";

    private static final String C_NORMAL_QUEUE = "c_normal_queue";

    private static final String DEAD_QUEUE = "dead_queue";

    //声明普通交换机
    @Bean("normalExchange")
    public DirectExchange normalExchange(){
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    //声明死信交换机
    @Bean("deadExchange")
    public DirectExchange deadExchange(){
        return new DirectExchange(DEAD_EXCHANGE);
    }

    //声明普通队列
    @Bean("aNormalQueue")
    public Queue aNormalQueue(){
        HashMap<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信routingKEY
        arguments.put("x-dead-letter-routing-key","dead_key");
        //设置TTL
        arguments.put("x-message-ttl",10000);

        return QueueBuilder.durable(A_NORMAL_QUEUE).withArguments(arguments).build();
    }

    @Bean("bNormalQueue")
    public Queue bNormalQueue(){
        HashMap<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信routingKEY
        arguments.put("x-dead-letter-routing-key","dead_key");
        //设置TTL
        arguments.put("x-message-ttl",30000);

        return QueueBuilder.durable(B_NORMAL_QUEUE).withArguments(arguments).build();
    }

    @Bean("cNormalQueue")
    public Queue cNormalQueue(){
        HashMap<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信routingKEY
        arguments.put("x-dead-letter-routing-key","dead_key");

        return QueueBuilder.durable(C_NORMAL_QUEUE).withArguments(arguments).build();
    }

    //声明死信队列
    @Bean("deadQueue")
    public Queue deadQueue(){
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    //绑定关系
    @Bean
    public Binding queueABindingX(@Qualifier("aNormalQueue") Queue queue,
                                  @Qualifier("normalExchange") DirectExchange normalExchange){
        return BindingBuilder.bind(queue).to(normalExchange).with("a_normal_key");
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("bNormalQueue") Queue queue,
                                  @Qualifier("normalExchange") DirectExchange normalExchange){
        return BindingBuilder.bind(queue).to(normalExchange).with("b_normal_key");
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("cNormalQueue") Queue queue,
                                  @Qualifier("normalExchange") DirectExchange normalExchange){
        return BindingBuilder.bind(queue).to(normalExchange).with("c_normal_key");
    }

    @Bean
    public Binding queueDeadBindingX(@Qualifier("deadQueue") Queue queue,
                                  @Qualifier("deadExchange") DirectExchange deadExchange){
        return BindingBuilder.bind(queue).to(deadExchange).with("dead_key");
    }

}
