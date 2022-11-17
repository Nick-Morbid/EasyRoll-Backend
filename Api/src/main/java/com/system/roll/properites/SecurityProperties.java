package com.system.roll.properites;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "classpath:properties/security.properties")
public class SecurityProperties {
    @Value("${security.expiredTime}")
    private Long expiredTime;
    @Value("${security.tokenSecret}")
    private String  tokenSecret;

}
