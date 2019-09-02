/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */

import com.rabbitmq.client.*;
import com.xuecheng.test.rabbitmq.consumer.RabbitMQConsumerTestApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * ProducerTest
 *
 * @author guoxing
 * @date 9/2/2019 10:04 AM
 * @since 2.0.0
 **/
@SpringBootTest(classes = {RabbitMQConsumerTestApplication.class})
@RunWith(SpringRunner.class)
@Slf4j
public class ConsumerTest {

    //队列名称
    private static final String QUEUE = "helloworld";

    /**
     * 消费者步骤
     * 1:创建连接
     * 2:创建通道
     * 3:声明队列
     * 4:监听队列
     * 5:消费消息
     */
    @Test
    public void testConsumer() {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            //rabbitmq默认虚拟机名称为“/”，虚拟机相当于一个独立的mq服务器
            factory.setVirtualHost("/");
            //创建与RabbitMQ服务的TCP连接
            connection = factory.newConnection();
            //创建与Exchange的通道，每个连接可以创建多个通道，每个通道代表一个会话任务
            channel = connection.createChannel();
            /**
             * 声明队列，如果Rabbit中没有此队列将自动创建
             * param1:队列名称
             * param2:是否持久化
             * param3:队列是否独占此连接
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数
             */
            channel.queueDeclare(QUEUE, true, false, false, null);
            //定义消费方法
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                /**
                 * 消费者接收消息调用此方法
                 * @param consumerTag 消费者的标签，在channel.basicConsume()去指定
                 * @param envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志
                (收到消息失败后是否需要重新发送)
                 * @param properties
                 * @param body
                 * @throws IOException
                 **/
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body)
                        throws IOException {
                    //交换机
                    String exchange = envelope.getExchange();
                    //路由key
                    String routingKey = envelope.getRoutingKey();
                    //消息id
                    long deliveryTag = envelope.getDeliveryTag();
                    // 重传标志  (收到消息失败后是否需要重新发送)
                    boolean redeliver = envelope.isRedeliver();
                    //消息内容
                    String msg = new String(body, StandardCharsets.UTF_8);
                    log.info("receive message.." + msg);
                }
            };
            /**
             * 监听队列String queue, boolean autoAck,Consumer callback
             * 参数明细
             * 1、队列名称
             * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置
             为false则需要手动回复
             * 3、消费消息的方法，消费者接收到消息后调用此方法
             */
            channel.basicConsume(QUEUE, true, consumer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Test
    public void testWorkQueueModel() {

        //1、一条消息只会被一个消费者接收；
        //2、rabbit采用轮询的方式将消息是平均发送给消费者的；
        //3、消费者在处理完某条消息后，才会收到下一条消息。

        // 应用场景：对于 任务过重或任务较多情况使用工作队列可以提高任务处理的速度。

        // 多个消费者监听 一个 queue ; 每个消费者采用轮询的方式 获取 消息

        for (int i = 0; i < 4; i++) {
            testConsumer();
        }

    }

}
