package com.system.roll.handler.mapstruct;

import com.system.roll.entity.pojo.RollStatistics;
import com.system.roll.entity.vo.roll.SingleRollStatisticVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RollDataConvertor {

    @Mappings({
            @Mapping(source = "enrollNum",target = "enrollNum"),
            @Mapping(source = "attendanceNum",target = "attendanceNum"),
            @Mapping(source = "absenceNum",target = "absenceNum"),
            @Mapping(source = "leaveNum",target = "leaveNum"),
            @Mapping(source = "lateNum",target = "lateNum"),
            @Mapping(source = "date",target = "date")
    })
    RollStatistics SingleRollStatisticVoToRollStatistics(SingleRollStatisticVo statisticVo);
}
