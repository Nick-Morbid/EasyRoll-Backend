package com.system.roll.service.professor.impl;

import com.system.roll.constant.impl.OperationType;
import com.system.roll.controller.professor.ProfessorBaseController;
import com.system.roll.describer.annotation.Operation;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.service.professor.ProfessorBaseService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component(value = "professorBaseService")
public class ProfessorBaseServiceImpl implements ProfessorBaseService {

    @Override
    @Operation(type = OperationType.UPLOAD_COURSE)
    public CourseVo uploadCourse(ProfessorBaseController.CourseDto courseDto, MultipartFile studentList) {
        return null;
    }

    @Override
    @Operation(type = OperationType.UPDATE_COURSE)
    public void updateCourse(ProfessorBaseController.CourseDto courseDto, MultipartFile studentList) {

    }

    @Override
    @Operation(type = OperationType.DELETE_COURSE)
    public void deleteCourse(String courseId) {

    }

    @Override
    public CourseListVo getCourseList(String courseId) {
        return null;
    }
}
