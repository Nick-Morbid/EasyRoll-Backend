package com.system.roll.service;

import com.system.roll.entity.bo.JsCode2sessionBo;

/**
 * 微信小程序后台接口调用服务
 * */
public interface WxApiService {
    /**
     * 调用微信小程序提供的code to session的接口，传入登录凭证code，获取session_key和openid
     * */
    JsCode2sessionBo jsCode2session(String code);

}
