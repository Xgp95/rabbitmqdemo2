package com.rebbitmq.rabbitmqdemo2.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Xugp
 * @date: 2022/7/28 17:41
 * @description:
 */
//@Configuration
public class DelayedQueueConfig {
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    //声明延迟队列
    @Bean("delayedQueue")
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE_NAME).build();
    }

    //自定义交换机 我们在这里定义的是一个延迟交换机
    @Bean("delayedExchange")
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    //绑定队列delayedQueue到交换机delayedExchange
    @Bean
    public Binding delayedQueueBindingdelayedExchange(@Qualifier("delayedQueue") Queue queue,
                                                      @Qualifier("delayedExchange") CustomExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DELAYED_ROUTING_KEY).noargs();
//        return BindingBuilder.bind(queue).to(exchange).with("XB");
    }
}
