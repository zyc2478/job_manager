package com.zyc.jobmanager.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class Properties {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    public String getRedisHost(){
        return redisHost;
    }

    public int getRedisPort(){
        return redisPort;
    }
}

/*
redis:
  host: 59.110.169.156
  port: 6379

bbd_ds:
  init:
    mode: 1
    begin: 2018-09-01
    end: 2018-09-30

bbd:
  init:
    open_id: 7344c77f9a7f4f249bd9df04115171e6
    token:
    refresh_token:
    token_init: 6fb59584-a8c4-4218-9b5e-5e88edb46575
    expire_days: 2
    init_date: 2018-01-16 23\:47
 */