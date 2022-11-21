package com.system.roll.handler.mapstruct;

import com.system.roll.entity.pojo.Course;
import com.system.roll.entity.vo.course.CourseVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CourseConvertor {

    CourseConvertor INSTANCE = Mappers.getMapper(CourseConvertor.class);

    @Mappings({
            @Mapping(source = "id",target = "id"),
            @Mapping(source = "courseName",target = "name"),
            @Mapping(source = "enrollNum",target = "enrollNum"),
            @Mapping(source = "startWeek",target = "startWeek"),
            @Mapping(source = "endWeek",target = "endWeek"),
    })
    CourseVo convert(Course course);
}
