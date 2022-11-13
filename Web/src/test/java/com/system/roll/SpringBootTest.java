package com.system.roll;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.system.roll.entity.vo.Result;
import com.system.roll.security.jwt.JwtSecurityHandler;
import com.system.roll.uitls.HttpRequestUtil;
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
    @Test
    public void testHttpGet() throws IOException {
        String appid = "wxe06234d727d0baf0";
        String secret = "275855de1e79658b0a0921d3a96cce6b";
        String grant_type = "authorization_code";
        String js_code = "053ro50009YiUO1AVn100tiGV31ro50Y";
        Result<?> result = httpRequestUtil.httpGet("https://api.weixin.qq.com/sns/jscode2session", Map.of("appid", appid, "secret", secret, "grant_type", grant_type, "js_code", js_code));
        System.out.println(result.getStatus());
        System.out.println(result.getMessage());
        Map<String,Object> data = (Map<String, Object>) result.getData();
        System.out.println(data.get("session_key"));
        System.out.println(data.get("openid"));
    }
}
