package com.github.middleware.redis;

import com.github.middleware.channel.ChannelProvider;
import com.github.middleware.event.EventPublish;
import com.github.middleware.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import static com.github.middleware.utils.GsonUtils.toJsonString;

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
                if (type == MessageType.P2P) {
                    Long lpush = jedis.lpush(getEventName(), toJsonString(getMessageData()));
                    log.info("lpush {}", lpush);
                } else if (type == MessageType.P2M) {
                    Long publish = jedis.publish(getEventName(), toJsonString(getMessageData()));
                    log.info("publish {}", publish);
                }
            }
        });
    }
}
