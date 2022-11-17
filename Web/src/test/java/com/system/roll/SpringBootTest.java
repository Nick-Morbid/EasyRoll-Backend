package com.system.roll;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.system.roll.config.properties.AppletProperties;
import com.system.roll.entity.vo.Result;
import com.system.roll.security.jwt.JwtSecurityHandler;
import com.system.roll.service.WxApiService;
import com.system.roll.uitls.HttpRequestUtil;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.IdUtil;
import com.system.roll.utils.JsonUtil;
import org.apache.http.entity.StringEntity;
import org.apache.tomcat.util.buf.StringUtils;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.boot.test.context.SpringBootTest(webEnvironment = org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootTest {
    @Test
    void stringTest(){
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        System.out.println(StringUtils.join(list,','));
    }
}
