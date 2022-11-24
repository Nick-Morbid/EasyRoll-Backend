package com.system.roll.redis;

public interface StudentRedis {

    default String PinYin(){return "PinYinCollection";}

    default String StudentName(){return "StudentName";}

    /**
     * 保存拼音信息
     * */
    void savePinYin(String studentId,String[] pinYin);

    /**
     * 获取拼音信息
     * 如果暂无相关信息，则返回空数组
     * */
    String[] getPinYin(String studentId);
    /**
     * 保存名字信息
     * */
    void saveName(String studentId,String studentName);

    /**
     * 获取名字信息
     * */
    String getName(String studentId);
}
