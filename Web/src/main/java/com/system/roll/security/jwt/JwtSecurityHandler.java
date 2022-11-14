package com.system.roll.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Component(value = "JwtSecurityHandler")
@ConfigurationProperties(prefix = "security")
public class JwtSecurityHandler {

    private long expiredTime;

    private String tokenSecret;

    /**
     * @param id id信息
     * @param role 角色信息
     * @param departmentId 学院id
     * @return 返回生成的token
     * */
    public String getToken(String id,int role,String departmentId) {

        /*设置token的header*/
        Map<String ,Object> header = new HashMap<>(2);
        header.put("typ","JWT");
        header.put("alg","HS256");

        /*设置过期时间和加密算法*/
        Date date = new Date(System.currentTimeMillis()+expiredTime);
        Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
        /*插入头信息，返回token*/
        return JWT.create()
                .withHeader(header)
                .withClaim("id",id)
                .withClaim("role",role)
                .withClaim("departmentId",departmentId)
                .withExpiresAt(date).sign(algorithm);
    }
    /**
     * 验证签名
     * @param token 要验证的token
     * @return 如果token没错，会返回一个DecodedJWT对象，可以用来查询token中的数据；否则会报错
     * */
    public DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(tokenSecret)).build().verify(token);
    }
}
