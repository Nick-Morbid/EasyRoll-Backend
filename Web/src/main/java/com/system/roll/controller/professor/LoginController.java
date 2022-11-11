package com.system.roll.controller.professor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/professor/login")
@Slf4j
public class LoginController {
    /**
     * 验证教师身份
     *
     *
     * */
    @PostMapping("/auth")
    public void auth(@RequestBody AuthDto data){
        log.info("authId:{}",data.getAuthId());
        log.info("code:{}",data.getCode());
    }

    /**
     * 测试方法，之后删
     * */
    @PostMapping(value = "/test")
    public void test(@RequestBody Object data){
        System.out.println(data);
    }


    /*
    * ====================================一些数据传输对象的定义===========================================
    * */

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class AuthDto{
        /**
         * 授权id（后端生成）
         * */
        private Long authId;
        /**
         * 授权码（微信平台提供）
         * */
        private String code;
    }
}
