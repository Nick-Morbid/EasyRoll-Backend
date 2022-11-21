package com.system.roll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.system.roll.entity.pojo.Major;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MajorMapper extends BaseMapper<Major> {

    /**
     * 根据名称模糊查询
     * */
    List<String> selectListByNameLike(@Param(value = "majorName")String majorName);

    /**
     * 根据名称查询专业id
     * */
    String selectIdByName(@Param(value = "majorName")String majorName);
}
