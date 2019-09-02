/*
 * Copyright (c) 2019, crayonshinchanxingguo.com Inc. All Rights Reserved
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.xuecheng.test.rabbitmq.producer.RabbitMQProducerTestApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ProducerTest
 *
 * @author guoxing
 * @date 9/2/2019 10:04 AM
 * @since 2.0.0
 **/
@SpringBootTest(classes = {RabbitMQProducerTestApplication.class})
@RunWith(SpringRunner.class)
@Slf4j
public class ProducerTest {

    //队列名称
    private static final String QUEUE = "helloworld";

    /**
     * 生产者的步骤
     * 1:创建连接
     * 2:创建通道
     * 3:声明队列
     * 4:发送消息
     *
     * @throws IOException
     * @throws TimeoutException
     */
    @Test
    public void testProducer() throws IOException, TimeoutException {
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
             * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,
             *                                  Map<String, Object> arguments)
             * 声明队列，如果Rabbit中没有此队列将自动创建
             * queue:队列名称
             * durable:是否持久化
             * exclusive:队列是否独占此连接
             * autoDelete:队列不再使用时是否自动删除此队列  ;  如果 autoDelete 和 exclusive 都为true ,则可以创建临时队列
             * arguments:队列参数
             */
            channel.queueDeclare(QUEUE, true, false, false, null);
            String message = "helloworld小明" + System.currentTimeMillis();
            /**
             * 消息发布方法
             * param1：Exchange的名称，如果没有指定，则使用Default Exchange
             * param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列
             * param3:消息包含的属性
             * param4：消息体
             */
            /**
             * 这里没有指定交换机，消息将发送给默认交换机，每个队列也会绑定那个默认的交换机，但是不能显
             示绑定或解除绑定
             * 默认的交换机，routingKey等于队列名称
             */
            channel.basicPublish("", QUEUE, null, message.getBytes());
            log.info("Send Message is:'" + message + "'");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

}
