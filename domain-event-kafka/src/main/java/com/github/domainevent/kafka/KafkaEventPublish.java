package com.github.domainevent.kafka;

import com.github.domainevent.event.EventPublish;

/**
 * @description:
 * @author: sufeng
 * @create: 2019-11-21 15:02
 */
public class KafkaEventPublish extends EventPublish {

    KafkaChannelProvider kafkaChannelProvider;

    public KafkaEventPublish(KafkaChannelProvider channelProvider) {
        super(channelProvider);
        this.kafkaChannelProvider = channelProvider;
    }

    @Override
    public void publishMessage() {

    }
}
