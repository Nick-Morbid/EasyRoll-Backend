package com.system.roll.professor.impl;

import com.system.roll.constant.impl.Role;
import com.system.roll.entity.pojo.Professor;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.professor.ProfessorVo;
import com.system.roll.mapper.ProfessorMapper;
import com.system.roll.professor.ProfessorBaseService;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.IdUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Date;

@Component(value = "ProfessorBaseService")
public class ProfessorBaseServiceImpl implements ProfessorBaseService {

    @Resource
    private ProfessorMapper professorMapper;

    @Resource
    private IdUtil idUtil;

    @Resource
    private DateUtil dateUtil;

    @Override
    public ProfessorVo getProfessorInfo(String openId) {
        return null;
    }

    @Override
    public CourseListVo getCourseList(String courseId) {
        return null;
    }

    @Override
    public InfoVo register(InfoDto infoDto) {
        Professor professor = new Professor(idUtil.getId(),
                infoDto.getName(),
                infoDto.getOpenId(),
                infoDto.getDepartmentId(),
                Role.PROFESSOR,
                infoDto.getGrade());
        professorMapper.insert(professor);

        return new InfoVo(
                professor.getId(),
                professor.getProfessorName(),
                professor.getDepartmentId(),
                professor.getRole().getCode(),
                dateUtil.getWeek(new Date(System.currentTimeMillis())),
                professor.getGrade());
    }
}
