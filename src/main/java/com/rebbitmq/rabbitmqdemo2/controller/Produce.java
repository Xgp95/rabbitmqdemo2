package com.rebbitmq.rabbitmqdemo2.controller;

import com.rebbitmq.rabbitmqdemo2.callback.MyCallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @author: Xugp
 * @date: 2022/7/29 10:30
 * @description:
 */
@RestController
@RequestMapping("/confirm")
@Slf4j
public class Produce {
    private static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    MyCallBack myCallBack;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(myCallBack);
        /**
         * true：
         * 交换机无法将消息进行路由时，会将该消息返回给生产者
         * false：
         * 如果发现消息无法进行路由，则直接丢弃
         */
        rabbitTemplate.setMandatory(true);
        //设置回退消息交给谁处理
        rabbitTemplate.setReturnCallback(myCallBack);
    }

    @GetMapping("sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        //指定消息 id 为 1
        CorrelationData correlationData1 = new CorrelationData("1");
        String routingKey = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, message + "+" + routingKey, correlationData1);

        CorrelationData correlationData2 = new CorrelationData("2");
        routingKey = "key1";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, message + "+" + routingKey, correlationData2);
        log.info("发送消息内容:{}", message);
    }

}
