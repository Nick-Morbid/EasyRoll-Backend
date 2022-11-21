package com.system.roll.service.common;

import java.util.List;

public interface DepartmentService {
    /**
     * 查询部门名称列表
     * */
    List<String> getDepartmentNameList(String keyword);
}
