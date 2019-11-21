package com.github.domainevent.activemq;

import com.github.domainevent.event.EventPublish;
import com.github.domainevent.message.MessageType;
import com.github.domainevent.utils.GsonUtils;

import javax.jms.*;

/**
 * @Author zhangchao
 * @Date 2019/8/8 9:56
 * @Version v1.0
 */
public class ActiveMQEventPublish extends EventPublish {

    public ActiveMQEventPublish(ActiveMQChannelProvider channelProvider) {
        super(channelProvider);
    }

    @Override
    public void publishMessage() {
        MessageType messageType = getMessageData().getMessageType();
        String eventName = getEventName();
        if(messageType == MessageType.PRODUCERS_AND_CONSUMERS){
            publishMessageP2P(eventName);
        }else if(messageType == MessageType.PUBLISH_SUBSCRIBE){
            publishMessagePS(eventName);
        }
    }

    private void publishMessageP2P(String queueName) {
        ActiveMQChannelProvider channelProvider = (ActiveMQChannelProvider)getChannelProvider();
        ConnectionFactory connectionFactory = channelProvider.getConnectionFactory();
        Connection connection =  null;
        Session session =  null;
        MessageProducer producer =  null;
        try{
            //2.获取连接
            connection = connectionFactory.createConnection();
            //3.启动连接
            connection.start();
                 /*4.获取session  (参数1：是否启动事务,
                参数2：消息确认模式[
                AUTO_ACKNOWLEDGE = 1    自动确认
                CLIENT_ACKNOWLEDGE = 2    客户端手动确认
                DUPS_OK_ACKNOWLEDGE = 3    自动批量确认
                SESSION_TRANSACTED = 0    事务提交并确认
                ])*/
             session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //5.创建队列对象
            Queue queue = session.createQueue(queueName);
            //6.创建消息生产者
            producer = session.createProducer(queue);
            //7.创建消息
            TextMessage textMessage = session.createTextMessage(GsonUtils.toJsonString(getMessageData()));
            //8.发送消息
            producer.send(textMessage);
            //9.关闭资源
        }catch (Exception e){
            e.printStackTrace();
        }finally {
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

    private void publishMessagePS(String queueName) {
        ActiveMQChannelProvider channelProvider = (ActiveMQChannelProvider)getChannelProvider();
        ConnectionFactory connectionFactory = channelProvider.getConnectionFactory();
        Connection connection =  null;
        Session session =  null;
        MessageProducer producer =  null;
        try{
            //2.获取连接
            connection = connectionFactory.createConnection();
            //3.启动连接
            connection.start();
                 /*4.获取session  (参数1：是否启动事务,
                参数2：消息确认模式[
                AUTO_ACKNOWLEDGE = 1    自动确认
                CLIENT_ACKNOWLEDGE = 2    客户端手动确认
                DUPS_OK_ACKNOWLEDGE = 3    自动批量确认
                SESSION_TRANSACTED = 0    事务提交并确认
                ])*/
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //5.创建主题对象
            Topic topic = session.createTopic(queueName);
            //6.创建消息生产者
            //6.创建消息生产者
            producer = session.createProducer(topic);
            //7.创建消息
            TextMessage textMessage = session.createTextMessage(GsonUtils.toJsonString(getMessageData()));
            //8.发送消息
            producer.send(textMessage);
            //9.关闭资源
        }catch (Exception e){
            e.printStackTrace();
        }finally {
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
}
