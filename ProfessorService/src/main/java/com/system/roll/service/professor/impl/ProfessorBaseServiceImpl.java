package com.system.roll.service.professor.impl;

import com.system.roll.constant.impl.OperationType;
import com.system.roll.constant.impl.Role;
import com.system.roll.controller.professor.ProfessorBaseController;
import com.system.roll.describer.annotation.Operation;
import com.system.roll.entity.pojo.Professor;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.mapper.ProfessorMapper;
import com.system.roll.service.professor.ProfessorBaseService;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.EnumUtil;
import com.system.roll.utils.IdUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.Date;

@Component(value = "professorBaseService")
public class ProfessorBaseServiceImpl implements ProfessorBaseService {

    @Resource
    private ProfessorMapper professorMapper;

    @Resource
    private IdUtil idUtil;

    @Resource
    private DateUtil dateUtil;


    @Override
    @Operation(type = OperationType.UPLOAD_COURSE)
    public CourseVo uploadCourse(ProfessorBaseController.CourseDto courseDto, MultipartFile studentList) {
        return null;
    }

    @Override
    @Operation(type = OperationType.UPDATE_COURSE)
    public void updateCourse(ProfessorBaseController.CourseDto courseDto, MultipartFile studentList) {

    }

    @Override
    @Operation(type = OperationType.DELETE_COURSE)
    public void deleteCourse(String courseId) {

    }

    @Override
    public CourseListVo getCourseList(String courseId) {
        return null;
    }

    @Override
    public ProfessorBaseController.InfoVo register(ProfessorBaseController.InfoDto infoDto) {
        Professor professor = new Professor(idUtil.getId(),
                infoDto.getName(),
                infoDto.getOpenId(),
                infoDto.getDepartmentId(),
                Role.PROFESSOR,
                infoDto.getGrade());
        professorMapper.insert(professor);
        ProfessorBaseController.InfoVo infoVo = new ProfessorBaseController.InfoVo(
                professor.getId(),
                professor.getProfessorName(),
                professor.getDepartmentId(),
                professor.getRole().getCode(),
                dateUtil.getWeek(new Date(System.currentTimeMillis())),
                professor.getGrade());

        return infoVo;
    }
}
