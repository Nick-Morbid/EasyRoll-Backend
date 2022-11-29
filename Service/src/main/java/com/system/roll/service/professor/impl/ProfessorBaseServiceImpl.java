package com.system.roll.service.professor.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.constant.impl.Role;
import com.system.roll.entity.dto.professor.InfoDto;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.pojo.Delivery;
import com.system.roll.entity.pojo.Professor;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.professor.ProfessorVo;
import com.system.roll.handler.mapstruct.ProfessorConvertor;
import com.system.roll.mapper.DeliveryMapper;
import com.system.roll.mapper.DepartmentMapper;
import com.system.roll.mapper.ProfessorMapper;
import com.system.roll.redis.CourseRedis;
import com.system.roll.service.auth.WxApiService;
import com.system.roll.service.professor.ProfessorBaseService;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.EnumUtil;
import com.system.roll.utils.IdUtil;
import com.system.roll.webSocket.context.SocketContextHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource(name = "CourseRedis")
    private CourseRedis courseRedis;

    @Override
    public ProfessorVo getProfessorInfo(String openId) {
        LambdaQueryWrapper<Professor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Professor::getOpenId,openId);
        Professor professor = professorMapper.selectOne(wrapper);
        if (professor==null) throw new ServiceException(ResultCode.NOT_REGISTER);
        ProfessorVo professorVo = professorConvertor
                .professorToProfessorVo(professor)
                .setDepartmentName(departmentMapper.selectNameById(professor.getDepartmentId()))
                .setWeekDay(dateUtil.getWeekDay(new Date(System.currentTimeMillis())));
        /*教授需要返回其有教学的课程信息*/
        if (professor.getRole().equals(Role.PROFESSOR)){
            List<Delivery> deliveries = deliveryMapper.selectByMap(Map.of("professor_id", professor.getId()));
            List<ProfessorVo.CourseVo> courseVos = deliveries.stream().map(delivery -> new ProfessorVo.CourseVo().setId(delivery.getCourseId()).setName(courseRedis.getCourseName(delivery.getCourseId()))).collect(Collectors.toList());
            professorVo.setCourses(courseVos);
        }
        return professorVo;
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
    @Transactional
    public ProfessorVo register(InfoDto infoDto) {
        /*获取用户的openId*/
        String openId = wxApiService.jsCode2session(infoDto.getCode()).getOpenId();
        /*查询是否已经注册过*/
        LambdaQueryWrapper<Professor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Professor::getOpenId,openId);
        Professor professor1 = professorMapper.selectOne(wrapper);
        if (professor1!=null) throw new ServiceException(ResultCode.USER_ALREADY_EXISTS);
        /*查找是否已有相关记录*/
        wrapper.clear();
        wrapper.eq(Professor::getProfessorName,infoDto.getName());
        Professor professor2 = professorMapper.selectOne(wrapper);
        /*插入新记录*/
        Professor professor = new Professor()
                .setId(infoDto.getId())
                .setProfessorName(infoDto.getName())
                .setOpenId(openId)
                .setDepartmentId(departmentMapper.selectIdByNameAndGrade(infoDto.getDepartmentName(),2020))
                .setRole(enumUtil.getEnumByCode(Role.class,infoDto.getRole()));
        professorMapper.insert(professor);
        /*如果之前已有相关记录，则进行记录的更新*/
        if (professor2!=null){
            /*先删除原有的记录*/
            professorMapper.deleteById(professor2.getId());
            /*修改授课表中的记录*/
            deliveryMapper.updateProfessorId(professor2.getId(),professor.getId());
        }
//        ProfessorVo professorVo = professorConvertor.professorToProfessorVo(professor)
//                .setCurrentWeek(dateUtil.getWeek(new Date(System.currentTimeMillis())))
//                .setDepartmentName(departmentMapper.selectNameById(professor.getDepartmentId()));
        /*组装授课表*/
        ProfessorVo professorVo = getProfessorInfo(openId);

        /*判断是否为从授权端拉起的注册业务*/
        if (infoDto.getSocketId()!=null){
            /*通知前端已完成注册*/
            try {
                SocketContextHandler.getContext(infoDto.getSocketId()).sendMessage(ResultCode.SUCCESS,professorVo);
            } catch (IOException | EncodeException e) {
                throw new ServiceException(ResultCode.FAILED_TO_SEND_MESSAGE);
            }
        }
        return professorVo;
    }
}
