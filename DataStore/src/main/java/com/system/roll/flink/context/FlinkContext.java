package com.system.roll.flink.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * flink处理任务的上下文接口（自定义）
 * */
public class FlinkContext<T,K> implements Serializable {

    protected Map<String, List<T>> paramsListMap = new ConcurrentHashMap<>();
    protected Map<String, K> paramsMap = new ConcurrentHashMap<>();

    public boolean listContainKey(String key){
        return paramsListMap.containsKey(key);
    }

    public boolean containKey(String key){
        return paramsMap.containsKey(key);
    }

    public void listAdd(String key,T value){
        if (!paramsListMap.containsKey(key)) paramsListMap.put(key,new ArrayList<>());
        paramsListMap.get(key).add(value);
    }

    public void add(String key,K value){
        paramsMap.put(key,value);
    }

    public List<T> listGet(String key){
        if (!paramsListMap.containsKey(key)) paramsListMap.put(key,new ArrayList<>());
        return paramsListMap.get(key);
    }

    public K get(String key){
        return paramsMap.get(key);
    }

    public void listRemove(String key){
        paramsListMap.remove(key);
    }

    public void remove(String key){
        paramsMap.remove(key);
    }

    public Integer listLength(String key){
        if (paramsListMap.containsKey(key)) return paramsListMap.get(key).size();
        else return 0;
    }
}
