package com.github.domainevent.kafka;

import com.github.domainevent.channel.ChannelConfig;

import java.util.Properties;

/**
 * @description:
 * @author: sufeng
 * @create: 2019-11-21 14:53
 */
public class KafkaChannelConfig extends ChannelConfig {

    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;

    public KafkaChannelConfig(Properties properties) {
        super(properties);
        this.bootstrapServers = properties.getProperty("bootstrap.servers",null);;
        this.keySerializer = properties.getProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");;
        this.valueSerializer = properties.getProperty("value.serializer","org.apache.kafka.common.serialization.StringSerializer");;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }
}
