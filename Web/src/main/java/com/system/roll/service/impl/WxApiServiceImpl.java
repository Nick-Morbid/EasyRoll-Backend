package com.system.roll.service.impl;

import com.system.roll.config.properties.AppletProperties;
import com.system.roll.constant.impl.ResultCode;
import com.system.roll.entity.bo.JsCode2sessionBo;
import com.system.roll.entity.vo.Result;
import com.system.roll.exception.impl.ServiceException;
import com.system.roll.service.WxApiService;
import com.system.roll.uitls.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component(value = "WxApiService")
public class WxApiServiceImpl implements WxApiService {

    @Resource
    private HttpRequestUtil httpRequestUtil;
    @Resource
    private AppletProperties appletProperties;

    @Override
    @SuppressWarnings("all")
    public JsCode2sessionBo jsCode2session(String code) {
        Result<?> result;
        try {
             result = httpRequestUtil.httpGet(appletProperties.getJsCode2session(),
                    Map.of("appid", appletProperties.getAppId(),
                            "secret", appletProperties.getSecret(),
                            "grant_type", "authorization_code",
                            "js_code", code));
        } catch (IOException e) {
            log.error(e.getCause().toString());
            throw new ServiceException(ResultCode.API_CALLED_FAILED);
        }
        Map<String,Object> data = (Map<String, Object>)result.getData();
        if (data.containsKey("openid")) {
            return new JsCode2sessionBo()
                    .setOpenId(String.valueOf(data.get("openid")))
                    .setSessionKey(String.valueOf(data.get("session_key")));
        }else {
            throw new ServiceException(ResultCode.CODE_VOID);
        }
    }
}
