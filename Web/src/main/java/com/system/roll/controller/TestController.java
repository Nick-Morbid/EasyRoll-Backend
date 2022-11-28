package com.system.roll.controller;

import com.system.roll.context.common.CommonContext;
import com.system.roll.entity.dto.student.CourseDto;
import com.system.roll.entity.vo.course.CourseVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    @Resource
    private CommonContext commonContext;

    @PostMapping("/upload")
    public CourseVo uploadCourse(
            @RequestParam(value = "courseName") String courseName,
            @RequestParam(value = "professorName") String professorName,
            @RequestParam(value = "classroomNo") Integer classroomNo,
            @RequestParam(value = "startWeek") Integer startWeek,
            @RequestParam(value = "endWeek") Integer endWeek,
            @RequestParam(value = "grade") Integer grade,
            @RequestParam(value = "courseArrangements") String courseArrangements,
            @RequestParam(value = "studentList") MultipartFile studentList
    ){
        CourseDto courseDto = new CourseDto()
                .setCourseName(courseName)
                .setProfessorName(professorName)
                .setClassroomNo(classroomNo)
                .setStartWeek(startWeek)
                .setEndWeek(endWeek)
                .setGrade(grade)
                .setCourseArrangements(courseArrangements)
                .setStudentList(studentList);
        System.out.println(courseDto);
        return null;
    }
}
