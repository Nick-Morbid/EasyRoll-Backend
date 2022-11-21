package com.system.roll.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 微信小程序jsCode2session接口返回结果封装
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class JsCode2sessionBo {
    private String sessionKey;
    private String openId;
}
