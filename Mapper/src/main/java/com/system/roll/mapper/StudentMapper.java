package com.system.roll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.system.roll.entity.constant.impl.Role;
import com.system.roll.entity.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    /**
     * 根据courseId查询出学生列表
     * */
    List<Student> selectListByCourseId(@Param(value = "courseId") String courseId);

    /**
     * 根据openId查询学生信息
     * */
    Student selectByOpenId(@Param(value = "openId")String openId);

    /**
     * 更新学生的角色
     * */
    void updateRole(@Param(value = "id") String id,@Param(value = "role") Role supervisor);
}
