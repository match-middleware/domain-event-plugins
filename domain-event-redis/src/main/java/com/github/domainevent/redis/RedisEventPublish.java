package com.github.domainevent.redis;

import com.github.domainevent.channel.ChannelProvider;
import com.github.domainevent.event.EventPublish;
import com.github.domainevent.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import static com.github.domainevent.utils.GsonUtils.toJsonString;

/**
 * @author: zhangchao
 * @time: 2018-12-25 10:43
 **/
public class RedisEventPublish extends EventPublish {

    public final static Logger log = LoggerFactory.getLogger(RedisEventPublish.class);

    public RedisEventPublish(ChannelProvider channelProvider) {
        super(channelProvider);
    }

    public void publishMessage() {
        RedisChannelProvider channelProvider = (RedisChannelProvider) getChannelProvider();
        RedisCmd redisTemplate = channelProvider.getRedisCmd();
        MessageType type = channelProvider.getType();
        redisTemplate.execute(new RedisCmd.Cmd() {
            public void cmd(Jedis jedis) {
                log.info("publish {}: {}:{}",type, getEventName(), toJsonString(getMessageData()));
                if (type == MessageType.PRODUCERS_AND_CONSUMERS) {
                    Long lpush = jedis.lpush(getEventName(), toJsonString(getMessageData()));
                    log.info("lpush {}", lpush);
                } else if (type == MessageType.PUBLISH_SUBSCRIBE) {
                    Long publish = jedis.publish(getEventName(), toJsonString(getMessageData()));
                    log.info("publish {}", publish);
                }
            }
        });
    }
}
