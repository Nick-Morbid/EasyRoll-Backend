package com.system.roll.properites;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 小程序配置读取
 * */
@Data
@Component
@PropertySource("classpath:properties/applet.properties")
public class AppletProperties {
    @Value("${applet.appId}")
    private String appId;
    @Value("${applet.secret}")
    private String secret;
    @Value("${applet.jsCode2session}")
    private String jsCode2session;
    @Value("${applet.accessToken}")
    private String accessToken;
    @Value("${applet.getWXACodeUnLimit}")
    private String getWXACodeUnLimit;
}
