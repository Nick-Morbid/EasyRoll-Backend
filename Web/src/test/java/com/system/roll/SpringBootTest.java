package com.system.roll;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.system.roll.security.jwt.JwtSecurityHandler;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

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
}
