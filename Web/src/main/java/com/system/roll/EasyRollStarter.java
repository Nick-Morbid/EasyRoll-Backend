package com.system.roll;

import com.system.roll.handler.InitializingHandler;
import com.system.roll.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@SpringBootApplication
@EnableTransactionManagement
public class EasyRollStarter {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(EasyRollStarter.class,args);
        InitializingHandler initializingHandler = SpringContextUtil.getBean("InitializingHandler");
        initializingHandler.init();
    }
}
