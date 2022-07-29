package com.rebbitmq.rabbitmqdemo2.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: Xugp
 * @date: 2022/7/29 12:35
 * @description:
 */
@Component
@Slf4j
public class BackupConsumer {
    public static final String BACKUP_QUEUE_NAME = "backup.queue";

    @RabbitListener(queues = BACKUP_QUEUE_NAME)
    public void receiveBackupMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("备份路由处理消息：{}", msg);
    }
}
