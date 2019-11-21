package com.github.domainevent.activemq;

import com.github.domainevent.channel.ChannelProvider;
import com.github.domainevent.event.EventPublish;
import com.github.domainevent.event.EventSubscriber;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.ConnectionFactory;

/**
 * @Author zhangchao
 * @Date 2019/8/8 9:52
 * @Version v1.0
 */
public class ActiveMQChannelProvider extends ChannelProvider<ActiveMQChannelConfig> {

    private ConnectionFactory connectionFactory;

    public ActiveMQChannelProvider(ActiveMQChannelConfig channelConfig) {
        super(channelConfig);
    }

    @Override
    public void init() {
        ActiveMQChannelConfig channelConfig = getChannelConfig();
        String brokerURL = String.format("tcp://%s:%s",channelConfig.getHost(),channelConfig.getPort());
        String username = channelConfig.getUsername();
        String password = channelConfig.getPassword();
        connectionFactory = new ActiveMQConnectionFactory(username,password,brokerURL);
    }

    public ConnectionFactory getConnectionFactory() {
        if(connectionFactory == null){
            init();
        }
        return connectionFactory;
    }

    @Override
    public EventSubscriber createEventSubscriber() {
        return new ActiveMQEventSubscriber(this);
    }

    @Override
    public EventPublish createEventPublish() {
        return new ActiveMQEventPublish(this);
    }
}
