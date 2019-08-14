package com.github.middleware.starter;

import com.github.middleware.EventStream;
import com.github.middleware.config.EventConfigItem;
import com.github.middleware.config.EventConfigManager;
import com.github.middleware.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zhangchao
 * @Date 2019/8/5 16:43
 * @Version v1.0
 */
@Configuration
public class MicroserviceStreamAutoConfiguration implements ApplicationContextAware {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(EventConfigItem.class).values().stream().forEach(v->{
            logger.info("add EventConfigItem ->{}",v.getEventName());
            EventConfigManager.addEventSubcriberConfigItem(v);
        });

        applicationContext.getBeansOfType(EventHandler.class).values().stream().forEach(v->{
            logger.info("event register ->{}",v.getEventName());
            EventStream.register(v);
        });
    }
}
