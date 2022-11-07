package com.system.roll.utils;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

/**
 * Json工具类
 * */
@Component
public class JsonUtil {
    private static final Gson gson = new Gson();
    public static<T> String toJson(T data){
        return gson.toJson(data);
    }

    public static<T> T toObject(String json,Class<T> tClass){
        return gson.fromJson(json,tClass);
    }
}
