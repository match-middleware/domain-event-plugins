package com.github.domainevent.activemq;

import com.github.domainevent.event.EventHandler;

/**
 * @Author zhangchao
 * @Date 2019/8/8 10:58
 * @Version v1.0
 */
public class ProductStoreMaagerSystem implements EventHandler<PaidEvent> {
    public void handler(PaidEvent s) {
        System.out.println("仓库系统 =>:"+s);
    }
    public String getEventName() {
        return PaidEvent.EVENT_NAME;
    }
}