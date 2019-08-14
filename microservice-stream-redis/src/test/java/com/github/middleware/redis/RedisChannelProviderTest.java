package com.github.middleware.redis;

import com.github.middleware.EventStream;
import com.github.middleware.channel.ChannelProvider;
import com.github.middleware.config.EventConfigItem;
import com.github.middleware.config.EventConfigManager;
import com.github.middleware.event.EventHandler;

import java.util.Properties;
import java.util.Scanner;

public class RedisChannelProviderTest {


    public static void main(String[] args) {


        Properties properties = new Properties();
        properties.setProperty("host","localhost:6379");
        RedisChannelConfig config = new RedisChannelConfig(properties);

        ChannelProvider channelProvider = new RedisChannelProvider(config);


        EventConfigItem a = new EventConfigItem();
        a.setChannelProvider(channelProvider);
        a.setEventName(A.EVENT_NAME);
        EventConfigManager.addEventSubcriberConfigItem(a);

        EventConfigItem b = new EventConfigItem();
        b.setChannelProvider(channelProvider);
        b.setEventName(B.EVENT_NAME);
        EventConfigManager.addEventSubcriberConfigItem(b);

        channelProvider.init();

        EventStream.register(new  A());
        EventStream.register(new  A());
        EventStream.register(new  A());
        EventStream.register(new  B());
        EventStream.register(new  B());
        EventStream.register(new  B());

        String eventName = "";
        Scanner scanner  = new Scanner(System.in);
        while (!(eventName = scanner.next()).equals("exit")){
            try {
                String[] split = eventName.split(":");
                EventStream.publish(split[0], split[1]);
            }catch (Exception e){

            }
        }
    }
}


class A implements EventHandler<String> {
    public static String EVENT_NAME = "A";
    public void handler(String s) {
        System.out.println(EVENT_NAME+"收到消息:"+s);
    }

    public String getEventName() {
        return EVENT_NAME;
    }
}




class B implements EventHandler<String>{
    public static String EVENT_NAME = "B";
    public void handler(String s) {
        System.out.println(EVENT_NAME+"收到消息:"+s);
    }

    public String getEventName() {
        return EVENT_NAME;
    }
}
