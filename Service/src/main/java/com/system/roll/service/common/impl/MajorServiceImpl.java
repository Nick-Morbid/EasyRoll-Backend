package com.system.roll.service.common.impl;

import com.system.roll.mapper.MajorMapper;
import com.system.roll.service.common.MajorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "MajorService")
public class MajorServiceImpl implements MajorService {

    @Resource
    private MajorMapper majorMapper;

    @Override
    public List<String> getMajorNameList(String keyword) {
        return majorMapper.selectListByNameLike(keyword);
    }
}
