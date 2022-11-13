package com.system.roll.service.impl;

import com.system.roll.entity.bo.JsCode2sessionBo;
import com.system.roll.service.WxApiService;
import org.springframework.stereotype.Component;

@Component(value = "WxApiService")
public class WxApiServiceImpl implements WxApiService {


    @Override
    public JsCode2sessionBo jsCode2session(String code) {

        return null;
    }
}
