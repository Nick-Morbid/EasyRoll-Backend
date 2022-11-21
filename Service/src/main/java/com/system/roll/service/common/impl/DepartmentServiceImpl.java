package com.system.roll.service.common.impl;

import com.system.roll.mapper.DepartmentMapper;
import com.system.roll.service.common.DepartmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "DepartmentService")
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    public List<String> getDepartmentNameList(String keyword) {
        return departmentMapper.selectListByNameLike(keyword);
    }
}
