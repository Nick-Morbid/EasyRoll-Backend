package com.system.roll;

import com.system.roll.handler.InitializingHandler;
import com.system.roll.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataStoreStarter {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(DataStoreStarter.class,args);
        InitializingHandler initializingHandler = SpringContextUtil.getBean("InitializingHandler");
        initializingHandler.init();
    }
}
