package com.github.middleware.redis;

import com.github.middleware.channel.ChannelProvider;
import com.github.middleware.event.EventPublish;
import com.github.middleware.event.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: zhangchao
 * @time: 2018-12-24 17:28
 **/
public class RedisChannelProvider extends ChannelProvider<RedisChannelConfig> {

    public final static Logger log = LoggerFactory.getLogger(RedisChannelProvider.class);

    private Pool pool;
    private RedisCmd redisCmd;

    public RedisChannelProvider(RedisChannelConfig channelConfig) {
        super(channelConfig);
    }

    public void init() {
        RedisChannelConfig redisChannelConfig = getChannelConfig();
        Set<String> hosts = new HashSet<String>();
        hosts.addAll(redisChannelConfig.getHosts());
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisChannelConfig.getMaxIdle());
        config.setMaxTotal(redisChannelConfig.getMaxTotal());
        if (hosts.size() > 1) {
            pool = new JedisSentinelPool(redisChannelConfig.getMasterName(), hosts, config);
        } else {
            Iterator<String> iterator = hosts.iterator();
            if (iterator.hasNext()) {
                String next = iterator.next();
                String[] hostAndPort = HostAndPort.extractParts(next);
                log.info("connect host:{},port:{}", hostAndPort[0], hostAndPort[1]);
                pool = new JedisPool(config, hostAndPort[0], Integer.valueOf(hostAndPort[1]));
            }
        }
        redisCmd = new RedisCmd(pool);
    }

    public RedisCmd getRedisCmd() {
        return redisCmd;
    }

    public EventSubscriber createEventSubscriber() {
        return new RedisEventSubscriber(this);
    }

    public EventPublish createEventPublish() {
        return new RedisEventPublish(this);
    }
}
