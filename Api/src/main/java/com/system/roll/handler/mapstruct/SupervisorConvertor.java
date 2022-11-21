package com.system.roll.handler.mapstruct;

import com.system.roll.entity.pojo.Professor;
import com.system.roll.entity.pojo.Student;
import com.system.roll.entity.vo.supervisor.SupervisorVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface SupervisorConvertor {

    @Mappings({
            @Mapping(source = "professorName",target = "name"),
            @Mapping(expression = "java(professor.getRole().getCode())",target = "role")
    })
    SupervisorVo professorToSupervisorVo(Professor professor);

    @Mappings({
            @Mapping(source = "studentName",target = "name"),
            @Mapping(expression = "java(student.getRole().getCode())",target = "role")
    })
    SupervisorVo studentToSupervisorVo(Student student);
}
