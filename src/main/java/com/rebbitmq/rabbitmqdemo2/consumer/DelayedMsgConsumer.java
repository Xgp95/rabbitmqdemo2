package com.rebbitmq.rabbitmqdemo2.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import sun.rmi.runtime.Log;

import java.util.Date;

/**
 * @author: Xugp
 * @date: 2022/7/28 18:05
 * @description:
 */
@Slf4j
//@Component
public class DelayedMsgConsumer {
    @RabbitListener(queues = "delayed.queue")
    public void receiveDelayedQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到延迟队列信息{}",new Date(), msg);
    }
}
