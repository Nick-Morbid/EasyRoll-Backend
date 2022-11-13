package com.system.roll.service;

import com.system.roll.entity.bo.JsCode2sessionBo;

/**
 * 微信小程序后台接口调用服务
 * */
public interface WxApiService {
    JsCode2sessionBo jsCode2session(String code);
}
