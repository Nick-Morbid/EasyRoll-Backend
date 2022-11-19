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
    public CourseVo uploadCourse(@RequestBody CourseDTO courseDTO){
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
        private List<CourseArrangement> courseArrangements;
        private MultipartFile studentList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    @ToString
    public static class CourseArrangement {
        private Integer weekDay;
        private Integer period;
        private Integer mode;
    }
}


