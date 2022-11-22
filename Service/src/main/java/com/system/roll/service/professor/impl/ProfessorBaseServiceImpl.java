package com.system.roll.service.professor.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.entity.constant.impl.Role;
import com.system.roll.entity.dto.professor.InfoDto;
import com.system.roll.entity.pojo.Professor;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.professor.InfoVo;
import com.system.roll.entity.vo.professor.ProfessorVo;
import com.system.roll.handler.mapstruct.ProfessorConvertor;
import com.system.roll.mapper.DeliveryMapper;
import com.system.roll.mapper.DepartmentMapper;
import com.system.roll.mapper.ProfessorMapper;
import com.system.roll.service.auth.WxApiService;
import com.system.roll.service.professor.ProfessorBaseService;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.EnumUtil;
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

    @Resource(name = "WxApiService")
    private WxApiService wxApiService;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private ProfessorConvertor professorConvertor;

    @Override
    public ProfessorVo getProfessorInfo(String openId) {
        return null;
    }

    @Override
    public CourseListVo getCourseList(String courseId) {
        return null;
    }

    @Resource
    private EnumUtil enumUtil;

    @Resource
    private DeliveryMapper deliveryMapper;

    @Override
    public InfoVo register(InfoDto infoDto) {
        /*获取用户的openId*/
        String openId = wxApiService.jsCode2session(infoDto.getCode()).getOpenId();
        /*查找是否已有相关记录*/
        LambdaQueryWrapper<Professor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Professor::getProfessorName,infoDto.getName());
        Professor professor1 = professorMapper.selectOne(wrapper);
        /*插入新记录*/
        Professor professor = new Professor()
                .setId(idUtil.getId())
                .setProfessorName(infoDto.getName())
                .setOpenId(openId)
                .setDepartmentId(departmentMapper.selectIdByNameAndGrade(infoDto.getDepartmentName(),2020))
                .setRole(enumUtil.getEnumByCode(Role.class,infoDto.getRole()));
        professorMapper.insert(professor);
        /*如果之前已有相关记录，则进行记录的更新*/
        if (professor1!=null){
            /*先删除原有的记录*/
            professorMapper.deleteById(professor1.getId());
            /*修改授课表中的记录*/
            deliveryMapper.updateProfessorId(professor1.getId(),professor.getId());
        }
        return professorConvertor.professorToInfoVo(professor)
                .setCurrentWeek(dateUtil.getWeek(new Date(System.currentTimeMillis())))
                .setDepartmentName(departmentMapper.selectNameById(professor.getDepartmentId()));
    }
}
