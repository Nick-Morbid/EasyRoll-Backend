package com.system.roll.service;

import com.system.roll.controller.SupervisorBaseController;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;

public interface SupervisorBaseService {
    CourseListVo getAllCourse();

    void deleteCourse(String courseId);

    CourseVo uploadCourse(SupervisorBaseController.CourseDTO courseDTO);
}
