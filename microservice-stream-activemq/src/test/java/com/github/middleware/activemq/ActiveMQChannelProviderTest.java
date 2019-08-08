package com.github.middleware.activemq;


import com.github.middleware.Stream;
import com.github.middleware.annotation.SubscribeType;
import com.github.middleware.channel.ChannelProvider;
import com.github.middleware.config.EventConfigItem;
import com.github.middleware.config.EventConfigManager;
import com.github.middleware.utils.SubscribeTypeUtils;

import java.util.Properties;
import java.util.Scanner;

public class ActiveMQChannelProviderTest {


    public static void main(String[] args) {
//        Class<?> aClass = OrderManagerSystem.class;
//        SubscribeType subScribeType = SubscribeTypeUtils.findSubScribeType(aClass);
//        System.out.println(subScribeType.type());


        Properties properties = new Properties();
        ActiveMQChannelConfig config = new ActiveMQChannelConfig(properties);
        ChannelProvider channelProvider = new ActiveMQChannelProvider(config);


        EventConfigItem a = new EventConfigItem();
        a.setChannelProvider(channelProvider);
        a.setEventName(PaidEvent.EVENT_NAME);
        EventConfigManager.addEventSubcriberConfigItem(a);


        channelProvider.init();

        Stream.register(new  OrderManagerSystem());
        Stream.register(new  ProductStoreMaagerSystem());
        Stream.register(new  EmailMaagerSystem());

        String input = "";
        Scanner scanner  = new Scanner(System.in);
        while (!(input = scanner.next()).equals("exit")){
            try {
                String eventName = input.substring(0, input.indexOf(":"));
                String body = input.substring(input.indexOf(":")+1);
                Stream.publish(eventName, new PaidEvent(body));
            }catch (Exception e){

            }
        }
    }
}









