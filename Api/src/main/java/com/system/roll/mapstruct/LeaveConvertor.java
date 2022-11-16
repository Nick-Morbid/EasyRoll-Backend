package com.system.roll.mapstruct;

import com.system.roll.entity.pojo.Leave;
import com.system.roll.entity.vo.leave.LeaveVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LeaveConvertor {

    LeaveConvertor INSTANCE = Mappers.getMapper(LeaveConvertor.class);

    @Mappings({
            @Mapping(source = "id",target = "id"),
            @Mapping(source = "startTime",target = "startTime"),
            @Mapping(source = "endTime",target = "endTime"),
            @Mapping(source = "result",target = "result")
    })
    LeaveVo LeaveToLeaveVo(Leave leave);
}
