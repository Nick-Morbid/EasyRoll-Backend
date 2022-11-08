package com.system.roll.controller;

import com.system.roll.entity.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    @GetMapping("/test01")
    public User test01(){
        return new User().setId("1").setName("nick").setPassword("123");
    }
}
