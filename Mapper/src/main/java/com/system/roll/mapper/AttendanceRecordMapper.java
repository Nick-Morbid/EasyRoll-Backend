package com.system.roll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.system.roll.entity.pojo.AttendanceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository(value = "AttendanceRecordMapper")
public interface AttendanceRecordMapper extends BaseMapper<AttendanceRecord> {
}
