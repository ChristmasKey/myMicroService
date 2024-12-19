package com.stone.mq.spring;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    //@RabbitListener(queuesToDeclare = {@Queue(value = "simple.queue", durable = "false")})
    public void testSendMessage2SimpleQueue() {
        String queueName = "simple.queue";
        String message = "hello, spring amqp";

        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void testSendMessage2WorkQueue() throws InterruptedException {
        String queueName = "simple.queue";
        String message = "hello, message__";
        for (int i = 0; i < 50; i++) {
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }
    }

    @Test
    public void testSendMessage2FanoutExchange() {
        // 交换机名称
        String exchangeName = "itcast.fanout";
        // 路由键（用于将消息从交换机路由到特定队列。）
        String routingKey = "";
        // 消息内容
        String message = "hello, fanout exchange";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }

    @Test
    public void testSendMessage2DirectExchange() {
        // 交换机名称
        String exchangeName = "itcast.direct";
        // 路由键（用于将消息从交换机路由到特定队列。）
        String routingKey = "blue";
        // 消息内容
        String message = "hello, blue";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
