package com.system.roll.redis;

public interface StudentRedis {

    default String PinYin(){return "pinYinCollection";}

    /**
     * 保存拼音信息
     * */
    void savePinYin(String studentId,String[] pinYin);

    /**
     * 获取拼音信息
     * 如果暂无相关信息，则返回空数组
     * */
    String[] getPinYin(String studentId);
}
