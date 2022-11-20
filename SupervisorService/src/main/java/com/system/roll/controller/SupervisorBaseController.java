package com.system.roll.controller;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.exception.impl.ServiceException;
import com.system.roll.service.SupervisorBaseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/supervisor/base")
public class SupervisorBaseController {

    @Autowired
    private SupervisorBaseService supervisorBaseService;

    @PostMapping("/course/upload")
    public CourseVo uploadCourse(
            @RequestParam(value = "courseName") String courseName,
            @RequestParam(value = "professorName") String professorName,
            @RequestParam(value = "classroomNo") Integer classroomNo,
            @RequestParam(value = "startWeek") Integer startWeek,
            @RequestParam(value = "endWeek") Integer endWeek,
            @RequestParam(value = "grade") Integer grade,
            @RequestParam(value = "courseArrangements") List<String> courseArrangements,
            @RequestPart(value = "studentList") MultipartFile studentList

    ){
        CourseDTO courseDTO = new CourseDTO()
                .setCourseName(courseName)
                .setProfessorName(professorName)
                .setClassroomNo(classroomNo)
                .setStartWeek(startWeek)
                .setEndWeek(endWeek)
                .setGrade(grade)
                .setCourseArrangements(courseArrangements)
                .setStudentList(studentList);

        return supervisorBaseService.uploadCourse(courseDTO);
    }

    @PutMapping("/course/update")
    public void updateCourse(){

    }

    @DeleteMapping("/course/delete")
    public void deleteCourse(@RequestBody Map<String,Object> data){
        if(!data.containsKey("courseId")) throw new ServiceException(ResultCode.BODY_NOT_MATCH);
        String courseId = String.valueOf(data.get("courseId"));
        supervisorBaseService.deleteCourse(courseId);
    }

    @GetMapping("/course/getAll")
    public CourseListVo getAllCourse(){
        return supervisorBaseService.getAllCourse();
    }

    @PostMapping("/register")
    public InfoVo register(InfoDto infoDto){
        return supervisorBaseService.register(infoDto);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    @ToString
    public static class CourseDTO{
        private String courseName;
        private String professorName;
        private Integer classroomNo;
        private Integer startWeek;
        private Integer endWeek;
        private Integer grade;
        private List<String> courseArrangements;
        private MultipartFile studentList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class InfoVo{
        private String id;
        private String name;
        private String departmentId;
        private String departmentName;
        private String majorId;
        private String major;
        private Integer grade;
        private Integer classNo;
        private Integer currentWeek;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class InfoDto{
        private String name;
        private String departmentId;
        private String majorId;
        private Integer role;
        private String openId;
        private Integer grade;
        private Integer classNo;
    }
}


