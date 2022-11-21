package com.system.roll.service.auth;


import com.system.roll.entity.bo.JsCode2sessionBo;

/**
 * 微信小程序后台接口调用服务
 * */
public interface WxApiService {
    /**
     * 调用微信小程序提供的code to session的接口，传入登录凭证code，获取session_key和openid
     * */
    JsCode2sessionBo jsCode2session(String code);

    /**
     * 调用微信小程序提供的token接口，获取access_token访问凭证
     * */
    String accessToken();

    /**
     * 调用微信消成提供的getGetWXACodeUnLimit接口，获取小程序跳转二维码
     * */
    String getGetWXACodeUnLimit(String socketId,String accessToken);

}
