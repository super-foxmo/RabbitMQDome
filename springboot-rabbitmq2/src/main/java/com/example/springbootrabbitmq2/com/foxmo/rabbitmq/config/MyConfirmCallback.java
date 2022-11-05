package com.example.springbootrabbitmq2.com.foxmo.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

//消息确认回调方法
@Component
@Slf4j
public class MyConfirmCallback implements RabbitTemplate.ConfirmCallback ,RabbitTemplate.ReturnsCallback{

    @Resource
    private RabbitTemplate rabbitTemplate;

    //@PostConstruct注解,在对象加载完依赖注入后执行它通常都是一些初始化的操作，
    // 但初始化可能依赖于注入的其他组件，所以要等依赖全部加载完再执行
    //rabbitmqTamplate是一个单实例，所以无论你注入多少次都是同一个实例，而callback的实现是需要rabbit调用的，所以要注入到rabbit里
    @PostConstruct
    //将当前类注入到RabbitTemplate中
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机消息确认回调方法
     * @param correlationData   保存回调消息的ID及相关信息
     * @param ack     交换机是否接收到消息 （ture：已接收    false：未接收）
     * @param cause     交换几位接受失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack){
            log.info("交换机已经接收到ID为 {} 的消息",id);
        }else{
            log.info("交换机未接收到ID为 {} 的消息，失败原因：{}",id,cause);
        }
    }


    //备份交换机的优先及级比队列回退高
    /**
     * 队列消息确认回调方法
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息{}，被交换机{}退回，退回原因：{}，routingKey：{}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(),
                returnedMessage.getReplyText(),
                returnedMessage.getRoutingKey());
    }
}
