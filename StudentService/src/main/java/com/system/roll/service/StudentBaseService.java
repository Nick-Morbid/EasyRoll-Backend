package com.system.roll.service;

import com.system.roll.entity.vo.course.CourseListVo;
import org.springframework.stereotype.Service;

public interface StudentBaseService {
    CourseListVo getAllCourse();
}
