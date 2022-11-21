package com.system.roll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.system.roll.entity.pojo.Professor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProfessorMapper extends BaseMapper<Professor> {
    /**
     * 根据openId查询
     * */
    Professor selectByOpenId(@Param(value = "openId") String openId);
}
