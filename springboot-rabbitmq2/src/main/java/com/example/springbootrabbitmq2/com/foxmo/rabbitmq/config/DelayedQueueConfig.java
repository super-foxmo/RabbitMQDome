package com.example.springbootrabbitmq2.com.foxmo.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class DelayedQueueConfig {
    public static final String DELAYED_QUEUE_NAME = "delayed_queue";

    public static final String DELAYED_EXCHANGE_NAME = "delayed_exchange";

    public static final String DELAYED_ROUTING_KEY = "delayed_key";

    //声明交换机
    @Bean("delayedExchange")
    public CustomExchange delayedExchange(){
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");

        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",true,false,arguments);
    }

    //声明队列
    @Bean("delayedQueue")
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //绑定
    @Bean
    public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedExchange") CustomExchange exchange,
                                                      @Qualifier("delayedQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
