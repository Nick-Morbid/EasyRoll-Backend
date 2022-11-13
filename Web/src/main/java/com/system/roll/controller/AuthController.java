package com.system.roll.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 负责学生端、督导队员端的授权登录
 * 负责web端的第三方授权验证
 * */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    /**
     * 学生端微信小程序登录
     * */
    @PostMapping(value = "/login/student")
    public void studentLogin(@RequestBody LoginFormDto data){

    }

    /**
     * 督导队员端微信小程序登录
     * */
    @PostMapping(value = "/login/supervisor")
    public void supervisorLogin(@RequestBody LoginFormDto data){

    }

    /**
     * web端进行第三方授权（第三方为督导队员端小程序）
     * */
    @PostMapping(value = "/login/web")
    public void webLogin(@RequestBody LoginFormDto data){

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class LoginFormDto{
        private Integer authId;
        private String code;
    }

}
