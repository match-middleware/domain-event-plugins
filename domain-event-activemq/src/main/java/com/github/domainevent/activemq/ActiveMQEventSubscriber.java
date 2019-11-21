package com.github.domainevent.activemq;

import com.github.domainevent.event.EventSubscriber;
import com.github.domainevent.message.MessageData;
import com.github.domainevent.message.MessageType;
import com.github.domainevent.utils.JDKTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Serializable;

import static com.github.domainevent.utils.GsonUtils.toJsonString;
import static com.github.domainevent.utils.GsonUtils.toObject;

/**
 * @Author zhangchao
 * @Date 2019/8/8 9:56
 * @Version v1.0
 */
public class ActiveMQEventSubscriber extends EventSubscriber {
    Logger log = LoggerFactory.getLogger(getClass());

    Thread thread;

    ActiveMQChannelProvider activeMQChannelProvider;

    Connection connection = null;
    Session session = null;
    MessageProducer producer = null;



    public ActiveMQEventSubscriber(ActiveMQChannelProvider activeMQChannelProvider) {
        super();
        this.activeMQChannelProvider = activeMQChannelProvider;
    }

    @Override
    public void start(MessageType type) {
        String eventName = eventHandler.getEventName();
        if (type == MessageType.PRODUCERS_AND_CONSUMERS) {
            startP2P(eventName);
        } else if (type == MessageType.PUBLISH_SUBSCRIBE) {
            startPS(eventName);
        }
    }

    private void startP2P(String queueName) {
        try {
            ConnectionFactory connectionFactory = activeMQChannelProvider.getConnectionFactory();
                //2.获取连接
            connection = connectionFactory.createConnection();
            //3.启动连接
            connection.start();
//            4.获取session
//            参数1：是否启动事务,
//            参数2：消息确认模式[
//            AUTO_ACKNOWLEDGE = 1    自动确认
//            CLIENT_ACKNOWLEDGE = 2    客户端手动确认
//            DUPS_OK_ACKNOWLEDGE = 3    自动批量确认
//            SESSION_TRANSACTED = 0    事务提交并确认
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //5.创建队列对象
            Queue queue = session.createQueue(queueName);
            //6.创建消息消费者
            MessageConsumer consumer = session.createConsumer(queue);
            //7.监听消息

            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("接收到消息:" + textMessage.getText());

                        MessageData messageData = (MessageData) toObject(textMessage.getText(), MessageData.class);
                        Class<?> eventDataObjectClass = JDKTypeUtils.getEventDataObjectClass(eventHandler.getClass());
                        Object data = toObject(String.valueOf(messageData.getData()), eventDataObjectClass);
                        log.info("MessageListener {}: {}:{}", MessageType.PRODUCERS_AND_CONSUMERS, queue, toJsonString(messageData));
                        eventHandler.handler((Serializable) data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            //9.关闭资源
            Thread.sleep(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startPS(String queueName) {
        try {
            ConnectionFactory connectionFactory = activeMQChannelProvider.getConnectionFactory();
            //2.获取连接
            connection = connectionFactory.createConnection();
            //3.启动连接
            connection.start();
//            4.获取session
//            参数1：是否启动事务,
//            参数2：消息确认模式[
//            AUTO_ACKNOWLEDGE = 1    自动确认
//            CLIENT_ACKNOWLEDGE = 2    客户端手动确认
//            DUPS_OK_ACKNOWLEDGE = 3    自动批量确认
//            SESSION_TRANSACTED = 0    事务提交并确认
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //5.创建主题对象
            Topic topic = session.createTopic(queueName);
            //6.创建消息消费者
            MessageConsumer consumer = session.createConsumer(topic);
            //7.监听消息

            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("接收到消息:" + textMessage.getText());
                        MessageData messageData = (MessageData) toObject(textMessage.getText(), MessageData.class);
                        Class<?> eventDataObjectClass = JDKTypeUtils.getEventDataObjectClass(eventHandler.getClass());
                        Object data = toObject(String.valueOf(messageData.getData()), eventDataObjectClass);
                        log.info("MessageListener {}: {}:{}", MessageType.PRODUCERS_AND_CONSUMERS, queueName, toJsonString(messageData));
                        eventHandler.handler((Serializable) data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            //9.关闭资源
            Thread.sleep(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (producer != null) {
            try {
                producer.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
