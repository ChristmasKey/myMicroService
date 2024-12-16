package com.stone.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SpringRabbitListener {

    //@RabbitListener(queues = "simple.queue")
    //public void listenSimpleQueue(String msg) {
    //    System.out.println("消费者接收到simple.queue的消息：【" + msg + "】");
    //}

    @RabbitListener(queues = "simple.queue")
    public void listenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到消息：【" + msg + "】 " + LocalTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "simple.queue")
    public void listenWorkQueue2(String msg) throws InterruptedException {
        // 使用err来区分两个方法中控制台打印输出的颜色
        System.err.println("消费者2......接收到消息：【" + msg + "】 " + LocalTime.now());
        Thread.sleep(100);
    }
}
