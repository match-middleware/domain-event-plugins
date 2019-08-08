package com.github.middleware.activemq;

import com.github.middleware.annotation.SubscribeType;
import com.github.middleware.message.MessageType;

import java.io.Serializable;

/**
 * @Author zhangchao
 * @Date 2019/8/8 10:51
 * @Version v1.0
 */

@SubscribeType(type = MessageType.PUBLISH_SUBSCRIBE)
public class PaidEvent implements Serializable {
    public static String EVENT_NAME = "PAID_EVENT";
    private String orderId;

    public PaidEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "{" +
                "orderId='" + orderId + '\'' +
                '}';
    }
}