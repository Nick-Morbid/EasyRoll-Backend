package com.system.roll.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Authorization {
    Map<String,Object> authInfoMap = new HashMap<>();

    public Authorization addInfo(String key,Object value){
        authInfoMap.put(key,value);
        return this;
    }

    public <T> T getInfo(Class<T> clazz,String key){
        return clazz.cast(authInfoMap.get(key));
    }
}
