package com.system.roll;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.system.roll.config.properties.AppletProperties;
import com.system.roll.entity.vo.Result;
import com.system.roll.security.jwt.JwtSecurityHandler;
import com.system.roll.uitls.HttpRequestUtil;
import com.system.roll.utils.JsonUtil;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTest {
    @Resource
    private JwtSecurityHandler jwtSecurityHandler;

    @Test
    public void testGetToken(){
//        System.out.println(jwtSecurityHandler.getExpiredTime());
//        System.out.println(jwtSecurityHandler.getTokenSecret());
//        System.out.println(jwtSecurityHandler.getToken(Map.of("id", 1L, "role", 0, "departmentId", 1L)));
    }

    @Test
    public void testVerify(){
        String token = jwtSecurityHandler.getToken(1,0,1);
        DecodedJWT verify = jwtSecurityHandler.verify(token);
        System.out.println(verify.getClaim("id").asInt());
        System.out.println(verify.getClaim("role").asInt());
        System.out.println(verify.getClaim("departmentId").asInt());
    }
    @Resource
    private HttpRequestUtil httpRequestUtil;
    @Resource
    private AppletProperties appletProperties;
    @Test
    public void testHttpGet() throws IOException {
        String appid = appletProperties.getAppId();
        String secret = appletProperties.getSecret();
        String grant_type = "authorization_code";
        String js_code = "083iF7Ga1hsAfE0zeaJa1zYkOt3iF7Gc";
        Result<?> result = httpRequestUtil.httpGet("https://api.weixin.qq.com/sns/jscode2session", Map.of("appid", appid, "secret", secret, "grant_type", grant_type, "js_code", js_code));
        System.out.println(result.getStatus());
        System.out.println(result.getMessage());
        Map<String,Object> data = (Map<String, Object>) result.getData();
        System.out.println(JsonUtil.toJson(result.getData()));
        System.out.println(data.get("errcode"));
        System.out.println(data.get("errmsg"));
        System.out.println(data.get("session_key"));
        System.out.println(data.get("openid"));
    }
}
