package com.github.middleware.activemq;

import com.github.middleware.channel.ChannelConfig;

import java.util.Properties;

/**
 * @Author zhangchao
 * @Date 2019/8/8 9:48
 * @Version v1.0
 */
public class ActiveMQChannelConfig extends ChannelConfig {

    private String host;
    private String port;
    private String username;
    private String password;


    public ActiveMQChannelConfig(Properties properties) {
        super(properties);
        this.host = properties.getProperty("host","127.0.0.1");
        this.port = properties.getProperty("port","61616");
        this.username = properties.getProperty("username","admin");
        this.password = properties.getProperty("password","admin");
    }


    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

}
