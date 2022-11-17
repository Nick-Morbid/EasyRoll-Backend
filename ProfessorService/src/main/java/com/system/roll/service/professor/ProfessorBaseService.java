package com.system.roll.service.professor;

import com.system.roll.controller.professor.ProfessorBaseController;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import org.springframework.web.multipart.MultipartFile;

public interface ProfessorBaseService {

    CourseVo uploadCourse(ProfessorBaseController.CourseDto courseDto, MultipartFile studentList);

    void updateCourse(ProfessorBaseController.CourseDto courseDto, MultipartFile studentList);

    void deleteCourse(String courseId);

    CourseListVo getCourseList(String courseId);
}
