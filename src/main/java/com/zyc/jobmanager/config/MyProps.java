package com.zyc.jobmanager.config;

import lombok.ToString;

/**
 *
 myprops:
 init_mode: 1
 init_begin: 2018-09-01
 init_end: 2018-09-30
 open-id: 7344c77f9a7f4f249bd9df04115171e6
 token:
 refresh_token:
 token_init: 6fb59584-a8c4-4218-9b5e-5e88edb46575
 expire_days: 2
 token_init_date: 2018-01-16 23\:47
 redis:
 host: 59.110.169.156
 port: 6379
 *
 */
//@Component
/*@Getter
@Setter*/
@ToString
public class MyProps {
    private int init_mode;
    private String init_begin;
    private String init_end;
    private String open_id;
    private String token;
    private String refresh_token;
    private String token_init;
    private int expire_days;
    private String token_init_date;
    private String redis_host;
    private int redis_port;
    private RedisConfig redis;

    public int getInit_mode() {
        return init_mode;
    }

    public void setInit_mode(int init_mode) {
        this.init_mode = init_mode;
    }

    public String getInit_begin() {
        return init_begin;
    }

    public void setInit_begin(String init_begin) {
        this.init_begin = init_begin;
    }

    public String getInit_end() {
        return init_end;
    }

    public void setInit_end(String init_end) {
        this.init_end = init_end;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getToken_init() {
        return token_init;
    }

    public void setToken_init(String token_init) {
        this.token_init = token_init;
    }

    public int getExpire_days() {
        return expire_days;
    }

    public void setExpire_days(int expire_days) {
        this.expire_days = expire_days;
    }

    public String getToken_init_date() {
        return token_init_date;
    }

    public void setToken_init_date(String token_init_date) {
        this.token_init_date = token_init_date;
    }

    public String getRedis_host() {
        return redis_host;
    }

    public void setRedis_host(String redis_host) {
        this.redis_host = redis_host;
    }

    public int getRedis_port() {
        return redis_port;
    }

    public void setRedis_port(int redis_port) {
        this.redis_port = redis_port;
    }

    public RedisConfig getRedis() {
        return redis;
    }

    public void setRedis(RedisConfig redis) {
        this.redis = redis;
    }
}