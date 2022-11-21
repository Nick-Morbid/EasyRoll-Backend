package com.system.roll.service.common;

import java.util.List;

public interface MajorService {
    /**
     * 查询专业名称列表
     * */
    List<String> getMajorNameList(String keyword);;
}
