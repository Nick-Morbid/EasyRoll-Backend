package com.system.roll.controller.supervisor;

import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.service.supervisor.SupervisorBaseService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
            @RequestParam(value = "courseArrangements") List<String> courseArrangements,
            @RequestPart(value = "studentList") MultipartFile studentList

    ){
        SupervisorBaseService.CourseDTO courseDTO = new SupervisorBaseService.CourseDTO()
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
    public SupervisorBaseService.InfoVo register(SupervisorBaseService.InfoDto infoDto){
        return supervisorBaseService.register(infoDto);
    }

}


