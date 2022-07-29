package com.rebbitmq.rabbitmqdemo2.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Xugp
 * @date: 2022/7/29 10:20
 * @description:
 */
@Configuration
public class ConfirmConfig {
    private static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    private static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    //声明备份交换机队列
    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    //声明备份 Exchange
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //绑定备份交换机和队列
    @Bean
    public Binding backupBinding(@Qualifier("backupQueue") Queue queue,
                                 @Qualifier("backupExchange") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    // 声明警告队列
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    // 声明报警队列绑定关系
    @Bean
    public Binding warningBinding(@Qualifier("warningQueue") Queue queue,
                                  @Qualifier("backupExchange") FanoutExchange
                                          backupExchange) {
        return BindingBuilder.bind(queue).to(backupExchange);
    }

    //声明confirmExchange
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        Map<String,Object> args = new HashMap<>();
        args.put("alternate-exchange",BACKUP_EXCHANGE_NAME);
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .withArguments(args)
                .build();
    }

    //声明confirmQueue队列
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        Map<String,Object> args = new HashMap<>();
        args.put("x-max-priority", 10);
        args.put("x-message-ttl", 10000);
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).withArguments(args).build();
    }

    //绑定确认交换机和队列
    @Bean
    public Binding queueBinding(@Qualifier("confirmQueue") Queue queue,
                                @Qualifier("confirmExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("key1");
    }
}
