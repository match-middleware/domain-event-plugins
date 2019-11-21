package com.github.domainevent.kafka;

import com.github.domainevent.channel.ChannelProvider;
import com.github.domainevent.event.EventPublish;
import com.github.domainevent.event.EventSubscriber;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

/**
 * @description:
 * @author: sufeng
 * @create: 2019-11-21 14:58
 */
public class KafkaChannelProvider extends ChannelProvider<KafkaChannelConfig> {

    Producer producer;

    public KafkaChannelProvider(KafkaChannelConfig channelConfig) {
        super(channelConfig);
    }

    @Override
    public void init() {
        Properties props = getChannelConfig().getProperties();
        props.put("acks", "0");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        producer = new KafkaProducer(props);
    }

    @Override
    public EventSubscriber createEventSubscriber() {
        return new KafkaEventSubscriber(this);
    }

    @Override
    public EventPublish createEventPublish() {
        return new KafkaEventPublish(this);
    }


    public Producer getProducer() {
        return producer;
    }
}
