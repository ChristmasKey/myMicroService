package com.stone.hello;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerTest {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1.建立连接
        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数：主机名、端口号、vhost、用户名、密码
        factory.setHost("192.168.157.128");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("stone");
        factory.setPassword("1234");
        // 1.2.建立连接
        Connection connection = factory.newConnection();

        // 2.创建通道Channel
        Channel channel = connection.createChannel();

        // 3.创建队列
        //（因为Publisher和Consumer的启动先后顺序不确定，所以这里创建队列是为了避免项目启动时队列还没有被创建出来的保险措施）
        //（如果队列已经被创建了，那么此队列不会被重复创建）
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);

        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 5.处理消息
                String message = new String(body);
                System.out.println("接收到消息：【" + message + "】");
            }
        });

        System.out.println("等待接收消息...");
    }
}
