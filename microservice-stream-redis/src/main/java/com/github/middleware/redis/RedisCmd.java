package com.github.middleware.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

/**
 * @author: zhangchao
 * @time: 2018-12-25 11:00
 **/
class RedisCmd {

    private Pool jedisPool;

    public RedisCmd(Pool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private Jedis getJedis(){
        if(jedisPool instanceof JedisPool){
            return ((JedisPool)jedisPool).getResource();
        }
        if(jedisPool instanceof JedisSentinelPool){
            return ((JedisSentinelPool)jedisPool).getResource();
        }
        return null;
    }

    public void execute(Cmd cmd) {
        Jedis jedis = getJedis();
        if(jedis == null){
            return;
        }
        try{
            cmd.cmd(jedis);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null ){
                jedis.close();
            }
        }
    }

    public interface Cmd {
        void cmd(Jedis jedis);
    }
}
