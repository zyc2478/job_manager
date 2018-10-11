package com.zyc.jobmanager.util;

import com.ppdai.open.core.OpenApiClient;
import com.ppdai.open.core.RsaCryptoHelper;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;


/**
 * 跳转到AC的oauth2.0联合登录
 * https://ac.ppdai.com/oauth2/login?AppID=7344c77f9a7f4f249bd9df04115171e6&ReturnUrl=http://bidbydebt.com/auth/gettoken
 * <p>
 */

public class InitUtil {

    private static String token = "";
    private static Jedis jedis;

    @Value("${bbd.open_id}")
    private static String openId;

    @Value("${redis.host}")
    private static String redisHost;

    @Value("${redis.port}")
    private static int redisPort;



    public static void init() throws Exception {
        TokenUtil tokenUtil = new TokenUtil();
        String appId = "7344c77f9a7f4f249bd9df04115171e6";
        String clientPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAM7lQkzlTjz0HOt/rMqYJcUb4NmTY+7zOGOVG4fSgWdUI4m5Q1IgU5ZuQoyavS6LtX1oDOdu+ul8t+xZ02XL7nPqjNqiM5vMT1xmS/CK7afG42nGmAxSJSnatsVmQTN7OtiBaHOP1yt6Z4t8I/g9vmtOdqbgMqtl51H6CmXQzmUfAgMBAAECgYEAxCPP8PpKa7q5WAEYHJCU7gJ57YqvaK6nvLB1AxRrnTvE3SIHUmpXE/u/l3By/RdxnUvxP86UjJmx+51ErfLhQMj40e0A3gm5GdXRsHgVhHxyV3cpK5TU366y4opnIyaKm9cHbOZIK/t823D5NovXWj1C/6J/GTAVjfo1Dcp1PKECQQD7mEBZSCwegGsPjtjPKx2iHVJ+QydUdnIDQ+HnI7yexVb3IyNj3M0sTQiD1EJvPxMangr3W2gRChXA7gOaz585AkEA0oSl3SbSw4KJNnQFPfbh+3nObYUIuzGnYvJLWZArbSS6b51mM1t6CWeocyM4XG3MsRWg2yh8SXS1eILf0ajPFwJBANqVWkK3S21skJdBueezQ9mWtBbybPcauM9RaLCSAcvHE1k/c/3M1YyJmL4/6UARgp17dXeWOIGlS2UE5KjZfTECQFuuxYXBKL1ZFmUOtlG8OcMJ02tDKwBLqbigCUziSudnvYJqrF3lkwqRiH1Mc3ldoG3nG30W7roXCAmKBewLdtUCQHNroh7XYmA80gp3iIFcZ/ucyZLhkg5ThC/VMyoGwOgmuMVRCbO4WlzPAQvnv4GSNx6BSD2ARoaqkbKI/Op36RE=";
        String serverPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1hTOFi/JjUD7gqTP7EqHiLmsgLaI3FsVApbIpDHhXTIhPcC3OVQyysbqvTJUOhbb8JMj4SSninBWkvd0PYJK+e7P6+6qJcbo6+pnUJUkRQZ+qE0i6dRwXtmUd3yIPISIM0JP99ALxhl81Uz6Z68GRxnBiySbel84pwYPIJV1sCwIDAQAB";
        OpenApiClient.Init(appId, RsaCryptoHelper.PKCSType.PKCS8, serverPublicKey, clientPrivateKey);

        jedis = new Jedis(redisHost, redisPort);
        token = tokenUtil.getToken();
    }
    public static String getToken(){
        return token ;
    }
    public static String getOpenId(){
        return openId;
    }
    public static Jedis getJedis(){
        return jedis;
    }
}