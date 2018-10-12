package com.zyc.jobmanager.util;

import com.ppdai.open.core.OpenApiClient;
import com.ppdai.open.core.RsaCryptoHelper;
import com.zyc.jobmanager.config.MyProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TokenUtil {

    MyProps props = ConfigUtil.getProps();

    RedisUtil redisUtil = new RedisUtil();

    //private Jedis jedis = redisUtil.getJedis();

    private String openId = props.getOpen_id();
    private String refresh_token = props.getRefresh_token();
    private String token_init = props.getToken_init();
    private int expireDays = props.getExpire_days();
    private String initDate = props.getToken_init_date();
    private String token;
    private Jedis jedis = redisUtil.getJedis();

    //private static Logger logger = Logger.getLogger(TokenUtil.class);

    public void genNewToken() throws Exception {

        init();
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

    public boolean determineTokenInitExists() throws IOException {
        return token_init.equals("");
    }

    public boolean determineRefreshDate() throws ParseException {

        int diffDate = getDiffDate();
        int leftDays = expireDays - diffDate % expireDays;
        return leftDays <= 1;
    }

    private int getDiffDate() throws ParseException {
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
        token = jedis.get("token");
        return token;
    }

    public void setToken(String newToken) {
        token = newToken;
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

    public void setRefreshTokenInit(String refreshTokenInit) {
        //String initRefreshToken = ConfUtil.getProperty("refresh_token_init");
        jedis.setex("refreshToken", 8640000, refreshTokenInit); //100天后过期
    }

    public void setTokenInit(String tokenInit) {
        //String initToken =  ConfUtil.getProperty("token_init");
        jedis.setex("token", 691200, tokenInit); //8天后过期
    }

    public void init() throws Exception {
        //TokenUtil tokenUtil = new TokenUtil();
        String appId = "7344c77f9a7f4f249bd9df04115171e6";
        String clientPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAM7lQkzlTjz0HOt/rMqYJcUb4NmTY+7zOGOVG4fSgWdUI4m5Q1IgU5ZuQoyavS6LtX1oDOdu+ul8t+xZ02XL7nPqjNqiM5vMT1xmS/CK7afG42nGmAxSJSnatsVmQTN7OtiBaHOP1yt6Z4t8I/g9vmtOdqbgMqtl51H6CmXQzmUfAgMBAAECgYEAxCPP8PpKa7q5WAEYHJCU7gJ57YqvaK6nvLB1AxRrnTvE3SIHUmpXE/u/l3By/RdxnUvxP86UjJmx+51ErfLhQMj40e0A3gm5GdXRsHgVhHxyV3cpK5TU366y4opnIyaKm9cHbOZIK/t823D5NovXWj1C/6J/GTAVjfo1Dcp1PKECQQD7mEBZSCwegGsPjtjPKx2iHVJ+QydUdnIDQ+HnI7yexVb3IyNj3M0sTQiD1EJvPxMangr3W2gRChXA7gOaz585AkEA0oSl3SbSw4KJNnQFPfbh+3nObYUIuzGnYvJLWZArbSS6b51mM1t6CWeocyM4XG3MsRWg2yh8SXS1eILf0ajPFwJBANqVWkK3S21skJdBueezQ9mWtBbybPcauM9RaLCSAcvHE1k/c/3M1YyJmL4/6UARgp17dXeWOIGlS2UE5KjZfTECQFuuxYXBKL1ZFmUOtlG8OcMJ02tDKwBLqbigCUziSudnvYJqrF3lkwqRiH1Mc3ldoG3nG30W7roXCAmKBewLdtUCQHNroh7XYmA80gp3iIFcZ/ucyZLhkg5ThC/VMyoGwOgmuMVRCbO4WlzPAQvnv4GSNx6BSD2ARoaqkbKI/Op36RE=";
        String serverPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1hTOFi/JjUD7gqTP7EqHiLmsgLaI3FsVApbIpDHhXTIhPcC3OVQyysbqvTJUOhbb8JMj4SSninBWkvd0PYJK+e7P6+6qJcbo6+pnUJUkRQZ+qE0i6dRwXtmUd3yIPISIM0JP99ALxhl81Uz6Z68GRxnBiySbel84pwYPIJV1sCwIDAQAB";
        OpenApiClient.Init(appId, RsaCryptoHelper.PKCSType.PKCS8, serverPublicKey, clientPrivateKey);
        //token = tokenUtil.getToken();
    }
}