package com.github.middleware.redis;

import com.github.middleware.event.EventSubscriber;
import com.github.middleware.message.MessageData;
import com.github.middleware.message.MessageType;
import com.github.middleware.utils.JDKTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.io.Serializable;
import java.util.Optional;

import static com.github.middleware.utils.GsonUtils.toObject;


/**
 * @author: zhangchao
 * @time: 2018-12-25 10:42
 **/
public class RedisEventSubscriber extends EventSubscriber {
    public final static Logger log = LoggerFactory.getLogger(RedisEventSubscriber.class);

    private RedisChannelProvider redisChannelProvider;


    private boolean running;

    public RedisEventSubscriber(RedisChannelProvider redisChannelProvider) {
        super();
        this.redisChannelProvider = redisChannelProvider;
    }

    public void start(MessageType type) {
        running = true;
        log.info("RedisEventSubscriber.start() => {},{}",type.name(),eventHandler.getEventName());
        if (type == MessageType.P2M) {
            new Thread(new Runnable() {
                public void run() {
                    RedisCmd redisTemplate = redisChannelProvider.getRedisCmd();
                    redisTemplate.execute(new RedisCmd.Cmd() {
                        public void cmd(Jedis jedis) {
                            jedis.subscribe(new JedisPubSub() {
                                @Override
                                public void onMessage(String channel, String message) {
                                    log.info("subscribe:{}:{}", channel, message);
                                    MessageData messageData = (MessageData) toObject(message, MessageData.class);
                                    Class<?> eventDataObjectClass = JDKTypeUtils.getEventDataObjectClass(eventHandler.getClass());
                                    Object data = toObject(String.valueOf(messageData.getData()), eventDataObjectClass);
                                    eventHandler.handler((Serializable) data);
                                }
                            }, getEventHandler().getEventName());
                        }
                    });
                }
            }).start();
        } else if (type == MessageType.P2P) {
            new Thread(new Runnable() {
                public void run() {
                    RedisCmd redisCmd = redisChannelProvider.getRedisCmd();
                    while (running) {
                        try {
                            redisCmd.execute(new RedisCmd.Cmd() {
                                public void cmd(Jedis jedis) {
                                    Optional<String> optional = Optional.ofNullable(jedis.rpop(getEventHandler().getEventName()));
                                    if (optional.isPresent()) {
                                        log.info("rpop:{}:{}",getEventHandler().getEventName(), optional.get());
                                        MessageData messageData = (MessageData) toObject(optional.get(), MessageData.class);
                                        Class<?> eventDataObjectClass = JDKTypeUtils.getEventDataObjectClass(eventHandler.getClass());
                                        Object data = toObject(String.valueOf(messageData.getData()), eventDataObjectClass);
                                        eventHandler.handler((Serializable) data);
                                    }
                                }
                            });
                            Thread.sleep(200);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        }
    }

    public void stop() {
        running = false;
        log.info("RedisEventSubscriber.stop() => {}",eventHandler.getEventName());
    }
}
