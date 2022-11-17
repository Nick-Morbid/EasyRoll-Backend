package com.system.roll.controller.professor;

import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.service.professor.ProfessorBaseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/professor/base")
public class ProfessorBaseController {

    @Resource
    private ProfessorBaseService professorBaseService;

    /**
     * 上传课程信息
     * */
    @PostMapping(value = "/course/upload")
    public CourseVo uploadCourse(
            @RequestParam(value = "courseName")String courseName,
            @RequestParam(value = "professorName")String professorName,
            @RequestParam(value = "classroomNo")Integer classroomNo,
            @RequestParam(value = "startWeek")Integer startWeek,
            @RequestParam(value = "endWeek")Integer endWeek,
            @RequestParam(value = "grade")Integer grade,
            @RequestParam(value = "courseArrangements")List<CourseArrangementDto> courseArrangements,
            @RequestParam(value = "studentList") MultipartFile studentList
    ){
        return professorBaseService.uploadCourse(new CourseDto(courseName,professorName,classroomNo,startWeek,endWeek,grade,courseArrangements),studentList);
    }

    /**
     * 修改课程信息
     * */
    @PutMapping(value = "/course/update")
    public void updateCourse(
            @RequestParam(value = "courseName")String courseName,
            @RequestParam(value = "professorName")String professorName,
            @RequestParam(value = "classroomNo")Integer classroomNo,
            @RequestParam(value = "startWeek")Integer startWeek,
            @RequestParam(value = "endWeek")Integer endWeek,
            @RequestParam(value = "grade")Integer grade,
            @RequestParam(value = "courseArrangements")List<CourseArrangementDto> courseArrangements,
            @RequestParam(value = "studentList") MultipartFile studentList
    ){
        professorBaseService.updateCourse(new CourseDto(courseName,professorName,classroomNo,startWeek,endWeek,grade,courseArrangements),studentList);
    }

    /**
     * 删除课程信息
     * */
    @DeleteMapping(value = "/course/delete")
    public void deleteCourse(@RequestParam(value = "courseId")String courseId){
        professorBaseService.deleteCourse(courseId);
    }

    /**
     * 获取课程列表
     * */
    @GetMapping(value = "/course/getAll")
    public CourseListVo getCourseList(@RequestParam(value = "courseId")String courseId){
        return professorBaseService.getCourseList(courseId);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class CourseArrangementDto{
        private Integer weekDay;
        public Integer period;
        private Integer mode;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class CourseDto{
       private String courseName;
       private String professorName;
       private Integer classroomNo;
       private Integer startWeek;
       private Integer endWeek;
       private Integer grade;
       private List<CourseArrangementDto> courseArrangements;
    }
}
