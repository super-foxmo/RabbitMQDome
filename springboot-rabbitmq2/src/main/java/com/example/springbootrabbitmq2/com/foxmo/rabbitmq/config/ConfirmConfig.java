package com.example.springbootrabbitmq2.com.foxmo.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";

    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";

    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";

    public static final String BACKUP_QUEUE_NAME = "backup_queue";

    public static final String WARNING_QUEUE_NAME = "warning_queue";

    public static final String CONFIRM_ROUTING_KEY = "confirm_routing_key";

    //声明交换机
    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true)
                .withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME).build();
    }

    //备份交换机
    @Bean("backupExchange")
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //声明队列
    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //备份队列
    @Bean("backupQueue")
    public Queue backupQueue(){
        return new Queue(BACKUP_QUEUE_NAME);
    }

    //报警队列
    @Bean("warningQueue")
    public Queue warningQueue(){
        return new Queue(WARNING_QUEUE_NAME);
    }

    //绑定
    @Bean
    public Binding confirmQueueBindingConfirmExchange(@Qualifier("confirmExchange") DirectExchange exchange,
                                               @Qualifier("confirmQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUTING_KEY);
    }

    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupExchange") FanoutExchange exchange,
                                                    @Qualifier("backupQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("backupExchange") FanoutExchange exchange,
                                                    @Qualifier("warningQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange);
    }
}
