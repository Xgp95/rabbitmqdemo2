package com.rebbitmq.rabbitmqdemo2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author: Xugp
 * @date: 2022/7/28 15:51
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("ttl")
public class MsgController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("sendMessage/{msg}")
    public void sendMsg(@PathVariable String msg) {
        log.info("当前时间：{},发送一条信息给两个 TTL 队列:{}", new Date(), msg);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自 ttl 为 10S 的队列: " + msg);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自 ttl 为 40S 的队列: " + msg);
    }

    @GetMapping("senExprirationMessage/{msg}/{ttlTime}")
    public void sendMsg(@PathVariable String msg, @PathVariable String ttlTime) {
        rabbitTemplate.convertAndSend("X", "XC", msg, correlationData -> {
            correlationData.getMessageProperties().setExpiration(ttlTime);
            return correlationData;
        });
        log.info("当前时间：{},发送一条时长{}毫秒 TTL 信息给队列 C:{}", new Date(), ttlTime, msg);
    }

//    @GetMapping("sendDelayedMessage/{msg}/{delayedTime}")
    public void sendMessage(@PathVariable String msg, @PathVariable Integer delayedTime) {
        rabbitTemplate.convertAndSend("delayed.exchange", "delayed.routingkey", msg, correlationData -> {
            correlationData.getMessageProperties().setDelay(delayedTime);
            return correlationData;
        });
        log.info("当前时间：{},发送一条时长{}毫秒 TTL 信息给队列 C:{}", new Date(), delayedTime, msg);
    }
}
