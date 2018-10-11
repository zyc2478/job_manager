package com.zyc.jobmanager.util;

import com.zyc.jobmanager.config.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

    //Redis的端口号
    //private static int PORT = 6379;

    //访问密码
    //private static String AUTH = "admin";

    //private static int TIMEOUT = 10000;
    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Autowired
    Properties properties;

    private JedisPool jedisPool = null;

    public RedisUtil(String redisHost,int redisPort){
        //printConfig();
        initConn(redisHost,redisPort);
        //System.out.println("pro.redis_host：" +  properties.getRedisHost());
    }

    public void printConfig(){
        System.out.println("RedisHost in pringConfig : " + this.redisHost);
        System.out.println("RedisPort in pringConfig : " + this.redisPort);
        System.out.println("RedisHost in pringConfig prop : " + properties.getRedisHost());
        System.out.println("RedisPort in pringConfig prop : " + properties.getRedisPort());
    }

    public void initConn(String redisHost, int redisPort){
        JedisPoolConfig config = new JedisPoolConfig();

        config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
        int MAX_TOTAL = 1024;
        config.setMaxTotal(MAX_TOTAL);
        int MAX_IDLE = 200;
        config.setMaxIdle(MAX_IDLE);
        int MAX_WAIT = 10000;
        config.setMaxWaitMillis(MAX_WAIT);
//            boolean TEST_ON_BORROW = true;
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);

        //jedisPool=new JedisPool(config,redis_host,redis_port,100000);
        //jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
        //String ADDR = "localhost";
        //jedisPool = new JedisPool(config, ADDR);

//        System.out.println("initMode is : " + initMode);
//        System.out.println("initBegin is : " + initBegin);
//        System.out.println("initEnd is :" + initEnd);
        printConfig();
        System.out.println("redisHost in RedisUtil is :" + redisHost);
        System.out.println("redisPort is RedisUtil is :" + redisPort);
        jedisPool=new JedisPool(config,redisHost,redisPort,100000);
    }

    /*
      初始化Redis连接池
     */


    /**
     * 获取Jedis实例
     *
     */
    public synchronized Jedis getJedis() {
        try {
            if (jedisPool != null) {
                return jedisPool.getResource();
            } else {
                return null;
                //return jedisPool;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /*
     *
      释放jedis资源
      @param jedis
    */

	public void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }
}