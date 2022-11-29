package com.system.roll.controller.supervisor;

import com.system.roll.entity.dto.student.CourseDto;
import com.system.roll.entity.dto.student.InfoDto;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.entity.vo.student.InfoVo;
import com.system.roll.service.supervisor.SupervisorBaseService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/supervisor/base")
public class SupervisorBaseController {

    @Resource(name = "SupervisorBaseService")
    private SupervisorBaseService supervisorBaseService;

    @PostMapping("/course/upload")
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
        return supervisorBaseService.uploadCourse(courseDto);
    }

    @PutMapping("/course/update")
    public CourseVo updateCourse(
            @RequestParam(value = "courseId") String courseId,
            @RequestParam(value = "courseName") String courseName,
            @RequestParam(value = "professorName") String professorName,
            @RequestParam(value = "classroomNo") Integer classroomNo,
            @RequestParam(value = "startWeek") Integer startWeek,
            @RequestParam(value = "endWeek") Integer endWeek,
            @RequestParam(value = "grade") Integer grade,
            @RequestParam(value = "courseArrangements") String courseArrangements,
            @RequestParam(value = "studentList",required = false) MultipartFile studentList
    ){
        CourseDto courseDTO = new CourseDto()
                .setId(courseId)
                .setCourseName(courseName)
                .setProfessorName(professorName)
                .setClassroomNo(classroomNo)
                .setStartWeek(startWeek)
                .setEndWeek(endWeek)
                .setGrade(grade)
                .setCourseArrangements(courseArrangements)
                .setStudentList(studentList);
        return supervisorBaseService.updateCourse(courseDTO);
    }

    @DeleteMapping("/course/delete")
    public void deleteCourse(@RequestParam(value = "courseId")String courseId){
        supervisorBaseService.deleteCourse(courseId);
    }

    @GetMapping("/course/getAll")
    public CourseListVo getAllCourse(){
        return supervisorBaseService.getAllCourse();
    }

    @PostMapping("/register")
    public InfoVo register(@RequestBody InfoDto infoDto){
        return supervisorBaseService.register(infoDto);
    }

}


