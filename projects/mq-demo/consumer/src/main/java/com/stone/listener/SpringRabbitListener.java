package com.stone.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SpringRabbitListener {

    //@RabbitListener(queues = "simple.queue")
    //public void listenSimpleQueue(String msg) {
    //    System.out.println("消费者接收到simple.queue的消息：【" + msg + "】");
    //}

    //@RabbitListener(queues = "simple.queue")
    //public void listenWorkQueue1(String msg) throws InterruptedException {
    //    System.out.println("消费者1接收到消息：【" + msg + "】 " + LocalTime.now());
    //    Thread.sleep(20);
    //}

    //@RabbitListener(queues = "simple.queue")
    //public void listenWorkQueue2(String msg) throws InterruptedException {
    //    // 使用err来区分两个方法中控制台打印输出的颜色
    //    System.err.println("消费者2......接收到消息：【" + msg + "】 " + LocalTime.now());
    //    Thread.sleep(100);
    //}

    //@RabbitListener(queues = "fanout.queue1")
    //public void listenFanoutQueue1(String msg) {
    //    System.out.println("消费者接收到fanout.queue1的消息：【" + msg + "】");
    //}

    //@RabbitListener(queues = "fanout.queue2")
    //public void listenFanoutQueue2(String msg) {
    //    System.out.println("消费者接收到fanout.queue2的消息：【" + msg + "】");
    //}

    //@RabbitListener(bindings = @QueueBinding(value = @Queue(name = "direct.queue1"),
    //        exchange = @Exchange(name = "itcast.direct", type = ExchangeTypes.DIRECT),
    //        key = {"red", "blue"}))
    //public void listenDirectQueue1(String msg) {
    //    System.out.println("消费者接收到direct.queue1的消息：【" + msg + "】");
    //}

    //@RabbitListener(bindings = @QueueBinding(value = @Queue(name = "direct.queue2"),
    //        exchange = @Exchange(name = "itcast.direct", type = ExchangeTypes.DIRECT),
    //        key = {"red", "yellow"}))
    //public void listenDirectQueue2(String msg) {
    //    System.out.println("消费者接收到direct.queue2的消息：【" + msg + "】");
    //}

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(name = "itcast.topic", type = ExchangeTypes.TOPIC),
            key = "china.#"))
    public void listenTopicQueue1(String msg) {
        System.out.println("消费者接收到topic.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "topic.queue2"),
            exchange = @Exchange(name = "itcast.topic", type = ExchangeTypes.TOPIC),
            key = "#.news"))
    public void listenTopicQueue2(String msg) {
        System.out.println("消费者接收到topic.queue2的消息：【" + msg + "】");
    }
}