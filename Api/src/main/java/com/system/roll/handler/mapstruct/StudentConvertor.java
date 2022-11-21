package com.system.roll.handler.mapstruct;

import com.system.roll.entity.dto.InfoDto;
import com.system.roll.entity.pojo.Student;
import com.system.roll.entity.vo.student.StudentRollListVo;
import com.system.roll.entity.vo.InfoVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StudentConvertor {

    StudentConvertor INSTANCE = Mappers.getMapper(StudentConvertor.class);

    @Mappings({
            @Mapping(source = "id",target = "id"),
            @Mapping(source = "studentName",target = "name")
    })
    StudentRollListVo.StudentRollVo studentToStudentRollVo(Student student);

    @Mappings({
            @Mapping(source = "studentName",target = "name")
    })
    InfoVo studentToInfoVo(Student student);
    @Mappings({
            @Mapping(source = "id",target = "id"),
            @Mapping(source = "name",target = "studentName"),
            @Mapping(source = "classNo",target = "classNo"),
    })
    Student infoDtoToStudent(InfoDto infoDto);
}
