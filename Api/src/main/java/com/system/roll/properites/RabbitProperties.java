package com.system.roll.properites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Configuration
@ConfigurationProperties(prefix = "rabbit")
public class RabbitProperties {
    private Integer ttl;

    public Integer getTotalTTL(){
        return ttl*60*1000;
    }
}
