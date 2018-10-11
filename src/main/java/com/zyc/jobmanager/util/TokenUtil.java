package com.zyc.jobmanager.util;

import com.ppdai.open.core.OpenApiClient;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.apache.log4j.Logger;
//import org.junit.Test;
@SuppressWarnings("deprecation")
public class TokenUtil {

    @Value("${bbd.init.open_id}")
    private static String openId;

    @Value("${bbd.init.token}")
    private static String token;

    @Value("${bbd.init.token_init}")
    private static String token_init;

    @Value("${bbd.init.refresh_token}")
    private static String refresh_token;

    @Value("${bbd.init.expire_days}")
    private static int expireDays;

    @Value("${bbd.init.init_date}")
    private static String initDate;

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    RedisUtil redisUtil = new RedisUtil(redisHost,redisPort);
    private static Jedis jedis;

    TokenUtil(){
        initData();
    }

    private void initData(){
        openId = "7344c77f9a7f4f249bd9df04115171e6";
        jedis = redisUtil.getJedis();
    }
/*    static {
        openId = "7344c77f9a7f4f249bd9df04115171e6";
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/



    //private static Logger logger = Logger.getLogger(TokenUtil.class);

    public static void genNewToken() throws Exception {

        InitUtil.init();
        //logger.info(determineRefreshDate());

        String tokenFromConf = refresh_token;

        String refreshToken;
        if (tokenFromConf.equals("")) {
            refreshToken = jedis.get("refreshToken");
        } else {
            refreshToken = tokenFromConf;
        }
        OpenApiClient.refreshToken(openId, refreshToken);
    }

    public static boolean determineTokenInitExists() throws IOException {
        return token_init.equals("");
    }

    public static boolean determineRefreshDate() throws ParseException {

        int diffDate = getDiffDate();
        int leftDays = expireDays - diffDate % expireDays;
        return leftDays <= 1;
    }

    private static int getDiffDate() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beginDate = df.parse(initDate);
        Date today = new Date();
        //Date today = df.parse("2017-10-23 12:10");
        //logger.info(today);
        long diffTime = today.getTime() - beginDate.getTime();
        long diffDate = (int) (diffTime / (1000 * 60 * 60 * 24));
        return (int) diffDate;
    }

/*    public static boolean determineRefreshTokenExpired() throws ParseException {

        int diffDate = getDiffDate();
        int leftDays = refreshTokenExpired - diffDate % refreshTokenExpired;
        return leftDays <= 1;
    }*/

    public String getToken() {
        System.out.println("redisHost in getToken: " + redisHost);
        System.out.println("redisPort in getToken: " + redisPort);
        token = jedis.get("token");
        return token;
    }

    public static void setToken(String newToken) {
        TokenUtil.token = newToken;
        jedis.setex("token", 691200, token);
    }

/*    public static String getRefreshToken() {
        refreshToken = jedis.get("refreshToken");
        return refreshToken;
    }*/

/*    public static void setRefreshToken(String newRefreshToken) {
        TokenUtil.refreshToken = newRefreshToken;
        jedis.setex("refreshToken", 8640000, refreshToken);
    }*/

    public static void setRefreshTokenInit(String refreshTokenInit) {
        //String initRefreshToken = ConfUtil.getProperty("refresh_token_init");
        jedis.setex("refreshToken", 8640000, refreshTokenInit); //100天后过期
    }

    public static void setTokenInit(String tokenInit) {
        //String initToken =  ConfUtil.getProperty("token_init");
        jedis.setex("token", 691200, tokenInit); //8天后过期
    }
}