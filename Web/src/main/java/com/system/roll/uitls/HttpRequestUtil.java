package com.system.roll.uitls;

import com.system.roll.entity.vo.Result;
import com.system.roll.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Http请求发送工具类
 * */
@Component
@Slf4j
public class HttpRequestUtil {

    /**
     * 发送含参数的GET请求
     * */
    @SuppressWarnings("all")
    public Result<?> httpGet(String url, Map<String,Object> params) throws IOException {
        /*创建HttpClient对象*/
        CloseableHttpClient client = HttpClientBuilder.create().build();
        StringBuffer urlBuilder = new StringBuffer(url);
        /*构造完整路径*/
        final int[] count = {0};
        params.keySet().forEach(key-> urlBuilder.append(count[0]++==0?'?':'&').append(key).append('=').append(params.get(key)));
        url = urlBuilder.toString();
        log.info("httpGet:{}",url);
        /*创建HttpGet对象*/
        HttpGet httpGet = new HttpGet(url);
        /*执行GET请求*/
        CloseableHttpResponse response = client.execute(httpGet);
        /*封装响应结果*/
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        Map<String,Object> data = null;
        if (entity!=null){
            String json = EntityUtils.toString(entity);
            log.info("httpGet status:{},message:{},data:{}",statusLine.getStatusCode(),statusLine.getReasonPhrase(),json);
            data = JsonUtil.toObject(json, HashMap.class);
        }
        /*释放资源*/
        client.close();
        response.close();
        return Result.success(statusLine.getReasonPhrase(),statusLine.getStatusCode(),data);
    }

}
