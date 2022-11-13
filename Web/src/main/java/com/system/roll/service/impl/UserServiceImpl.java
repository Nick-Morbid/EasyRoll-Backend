package com.system.roll.service.impl;

import com.system.roll.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "UserService")
public class UserServiceImpl implements UserService {
    @Override
    public void test() {
        log.info("threadId3:{}",Thread.currentThread().getId());
    }
}
