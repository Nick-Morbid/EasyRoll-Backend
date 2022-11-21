package com.system.roll.service.auth.impl;

import com.system.roll.service.auth.WxApiService;
import com.system.roll.entity.bo.JsCode2sessionBo;
import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.properites.AppletProperties;
import com.system.roll.entity.vo.Result;
import com.system.roll.utils.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.StringEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
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

    @Override
    @SuppressWarnings("all")
    public String accessToken() {
        Result<?> result;
        try {
            result = httpRequestUtil.httpGet(appletProperties.getAccessToken(),
                    Map.of("appid", appletProperties.getAppId(),
                    "secret", appletProperties.getSecret(),
                    "grant_type", "client_credential"));
        } catch (IOException e) {
            log.error(e.getCause().toString());
            throw new ServiceException(ResultCode.API_CALLED_FAILED);
        }
        Map<String,Object> data = (Map<String, Object>)result.getData();
        if (data.containsKey("access_token")){
            return (String) data.get("access_token");
        }else {
            throw new ServiceException(ResultCode.ACCESS_TOKEN_VOID);
        }
    }

    @Override
    public String getGetWXACodeUnLimit(String socketId,String accessToken) {
        Map<String,Object> param = new HashMap<>();
        param.put("access_token",accessToken);
        StringEntity body = httpRequestUtil.getStringEntityBuilder()
                .add("scene", socketId)
                .add("page", "pages/login/login")
                .add("check_path", false)
                .add("env_version", "trial")
                .build();
        Result<?> result;
        try {
            result = httpRequestUtil.httpPost(appletProperties.getGetWXACodeUnLimit(),param,body);
        } catch (IOException e) {
            log.error(e.getCause().toString());
            throw new ServiceException(ResultCode.API_CALLED_FAILED);
        }
        /*返回读取到的文件流（这里为字符串形式）*/
        return (String) result.getData();
    }
}
