package com.system.roll.controller.professor;

import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.professor.ProfessorBaseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/professor/base")
public class ProfessorBaseController {

    @Resource(name = "ProfessorBaseService")
    private ProfessorBaseService professorBaseService;

    /**
     * 上传课程信息
     * */
    @PostMapping("/register")
    public ProfessorBaseService.InfoVo register(@RequestBody ProfessorBaseService.InfoDto infoDto) {
        return professorBaseService.register(infoDto);
    }

    /**
     * 获取课程列表
     * */
    @GetMapping(value = "/course/getAll")
    public CourseListVo getCourseList(@RequestParam(value = "courseId")String courseId){
        return professorBaseService.getCourseList(courseId);
    }

}
