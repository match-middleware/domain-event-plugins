package com.github.domainevent.kafka;

import com.github.domainevent.event.EventSubscriber;
import com.github.domainevent.message.MessageType;

/**
 * @description:
 * @author: sufeng
 * @create: 2019-11-21 15:01
 */
public class KafkaEventSubscriber extends EventSubscriber {


    KafkaChannelProvider kafkaChannelProvider;

    public KafkaEventSubscriber(KafkaChannelProvider kafkaChannelProvider) {
        this.kafkaChannelProvider = kafkaChannelProvider;
    }

    @Override
    public void start(MessageType type) {
        String eventName = eventHandler.getEventName();
        if (type == MessageType.PRODUCERS_AND_CONSUMERS) {
        } else if (type == MessageType.PUBLISH_SUBSCRIBE) {
        }
    }





    @Override
    public void stop() {
        if (kafkaChannelProvider.getProducer() != null) {
            kafkaChannelProvider.getProducer().close();
        }
    }
}
