package com.system.roll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.system.roll.entity.pojo.Delivery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeliveryMapper extends BaseMapper<Delivery> {
    /**
     * 将professorId从id1改为id2
     * */
    void updateProfessorId(@Param(value = "id1") String id1,@Param(value = "id2") String id2);
}
