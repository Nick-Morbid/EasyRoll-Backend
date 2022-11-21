package com.system.roll.utils;

import com.google.gson.JsonObject;
import com.system.roll.entity.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
     * @param url 接口地址
     * @param params 参数
     * */
    @SuppressWarnings("all")
    public Result<?> httpGet(String url, Map<String,Object> params) throws IOException {
        /*创建HttpClient对象*/
        CloseableHttpClient client = HttpClientBuilder.create().build();
        StringBuffer urlBuilder = new StringBuffer(url);
        /*构造完整路径，设置url路径参数*/
        final int[] count = {0};
        if (params!=null) params.keySet().forEach(key-> urlBuilder.append(count[0]++==0?'?':'&').append(key).append('=').append(params.get(key)));
        url = urlBuilder.toString();
        log.info("httpGet:{}",url);
        /*创建HttpGet对象*/
        HttpGet httpGet = new HttpGet(url);
        /*执行GET请求*/
        CloseableHttpResponse response = client.execute(httpGet);
        /*封装响应结果*/
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        Object data = null;
        if (entity!=null){
            String responseData = EntityUtils.toString(entity);
            String header = response.getFirstHeader("Content-Type").toString();
            System.out.println(header);
            if (header.contains("text/html")){
                data = responseData;
            }else if (header.contains("application/json")||header.contains("text/plain")){
                log.info("httpGet status:{},message:{},data:{}",statusLine.getStatusCode(),statusLine.getReasonPhrase(),responseData);
                data = (Map<String,Object>)JsonUtil.toObject(responseData, HashMap.class);
            }else {
                log.warn("暂不支持该类型的返回结果");
            }
        }
        /*释放资源*/
        client.close();
        response.close();
        return Result.success(statusLine.getReasonPhrase(),statusLine.getStatusCode(),data);
    }

    /**
     * 发送含参数的post请求
     * @param url 接口地址
     * @param params url路径参数
     * @param body 请求体参数
     * */
    @SuppressWarnings("all")
    public Result<?> httpPost(String url, Map<String, Object> params, StringEntity body) throws IOException {
        /*创建HttpClient对象*/
        CloseableHttpClient client = HttpClientBuilder.create().build();
        StringBuffer urlBuilder = new StringBuffer(url);
        /*构造完整路径*/
        if (params.size()>0){
            final int[] count = {0};
            /*设置url路径参数*/
            params.keySet().forEach(key-> urlBuilder.append(count[0]++==0?'?':'&').append(key).append('=').append(params.get(key)));
        }
        url = urlBuilder.toString();
        log.info("httpPost:{}",url);
        /*创建HttpPost对象*/
        HttpPost httpPost = new HttpPost(url);
        /*设置post请求参数*/
        httpPost.setEntity(body);
        /*执行POST请求*/
        CloseableHttpResponse response = client.execute(httpPost);
        /*封装响应结果*/
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        Object data = null;
        if (entity!=null){
            String responseData = EntityUtils.toString(entity);
            String header = response.getFirstHeader("Content-Type").toString();
            /*如果返回结果为图片流*/
            if (header.contains("image/jpeg")||header.contains("image/png")){
                log.info("httpGet status:{},message:{}",statusLine.getStatusCode(),statusLine.getReasonPhrase());
                data = responseData;
                /*如果返回结果为json字符串*/
            }else if (header.contains("application/json")){
                log.info("httpGet status:{},message:{},data:{}",statusLine.getStatusCode(),statusLine.getReasonPhrase(),responseData);
                data = JsonUtil.toObject(responseData, HashMap.class);
            }else {
                log.warn("暂不支持该类型的返回结果");
            }
        }
        /*释放资源*/
        client.close();
        response.close();
        return Result.success(statusLine.getReasonPhrase(),statusLine.getStatusCode(),data);
    }



    /**
     * 构造StringEntity对象
     * */
    public StringEntityBuilder getStringEntityBuilder(){
        return new StringEntityBuilder();
    }

    /**
     * 下面的建造者对象用于生成StringEntity对象
     * */
    public static class StringEntityBuilder{
        private final JsonObject jsonObject = new JsonObject();

        private StringEntityBuilder(){}

        public StringEntityBuilder add(String key,Object value){
            if (value instanceof Integer|| value instanceof Long || value instanceof Double || value instanceof Float) jsonObject.addProperty(key, (Number) value);
            else if (value instanceof String) jsonObject.addProperty(key, (String) value);
            else if (value instanceof Boolean) jsonObject.addProperty(key, (Boolean) value);
            else if (value instanceof Character) jsonObject.addProperty(key, (Character) value);
            else jsonObject.add(key,JsonUtil.toJsonObject(value));
            return this;
        }

        public StringEntity build(){
            return new StringEntity(jsonObject.toString(), StandardCharsets.UTF_8);
        }

    }
}
