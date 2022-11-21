package com.system.roll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.system.roll.entity.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 根据名称模糊查询
     * */
    List<String> selectListByNameLike(@Param(value = "departmentName") String departmentName);

    /**
     * 根据名称和年级查询院系的id
     * */
    String selectIdByNameAndGrade(@Param(value = "departmentName") String departmentName,@Param(value = "grade") Integer grade);
}
