package com.system.roll;

import com.system.roll.security.jwt.JwtSecurityHandler;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Map;

@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTest {
    @Resource
    private JwtSecurityHandler jwtSecurityHandler;

    @Test
    public void testGetToken(){
//        System.out.println(jwtSecurityHandler.getExpiredTime());
//        System.out.println(jwtSecurityHandler.getTokenSecret());
        System.out.println(jwtSecurityHandler.getToken(Map.of("id", 1L, "role", 0, "departmentId", 1L)));
    }
}
