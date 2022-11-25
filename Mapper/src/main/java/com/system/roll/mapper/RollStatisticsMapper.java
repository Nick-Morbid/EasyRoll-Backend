package com.system.roll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.system.roll.entity.pojo.RollStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository(value = "RollStatisticsMapper")
public interface RollStatisticsMapper extends BaseMapper<RollStatistics> {
}
