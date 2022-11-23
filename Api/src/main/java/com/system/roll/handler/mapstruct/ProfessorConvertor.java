package com.system.roll.handler.mapstruct;

import com.system.roll.entity.pojo.Professor;
import com.system.roll.entity.vo.professor.InfoVo;
import com.system.roll.entity.vo.professor.ProfessorVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProfessorConvertor {


    @Mappings({
            @Mapping(source = "id",target = "id"),
            @Mapping(source = "professorName",target = "name"),
            @Mapping(source = "departmentId",target = "departmentId"),
            @Mapping(expression = "java(professor.getRole().getCode())",target = "role"),
    })
    InfoVo professorToInfoVo(Professor professor);

    @Mappings({
            @Mapping(source = "id",target = "id"),
            @Mapping(source = "professorName",target = "name"),
            @Mapping(expression = "java(professor.getRole().getCode())",target = "role"),
            @Mapping(source = "departmentId",target = "departmentId")
    })
    ProfessorVo professorToProfessorVo(Professor professor);
}
