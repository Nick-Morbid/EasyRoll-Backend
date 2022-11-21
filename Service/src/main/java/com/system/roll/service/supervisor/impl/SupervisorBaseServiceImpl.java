package com.system.roll.service.supervisor.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.context.security.SecurityContextHolder;
import com.system.roll.entity.constant.impl.Period;
import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.constant.impl.Role;
import com.system.roll.entity.constant.impl.TeachingMode;
import com.system.roll.entity.dto.supervisor.InfoDto;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.pojo.*;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.entity.vo.supervisor.InfoVo;
import com.system.roll.entity.vo.supervisor.SupervisorVo;
import com.system.roll.excel.annotation.Excel;
import com.system.roll.excel.uitl.ExcelUtil;
import com.system.roll.handler.mapstruct.StudentConvertor;
import com.system.roll.mapper.*;
import com.system.roll.service.auth.WxApiService;
import com.system.roll.service.supervisor.SupervisorBaseService;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.EnumUtil;
import com.system.roll.utils.IdUtil;
import lombok.Data;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(value = "SupervisorBaseService")
public class SupervisorBaseServiceImpl implements SupervisorBaseService {

    @Resource
    private DateUtil dateUtil;

    @Resource
    private IdUtil idUtil;

    @Resource
    private RollRelationMapper rollRelationMapper;

    @Resource
    private CourseArrangementMapper courseArrangementMapper;

    @Resource
    private DeliveryMapper deliveryMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private ProfessorMapper professorMapper;

    @Resource
    private CourseRelationMapper courseRelationMapper;

    @Resource
    private EnumUtil enumUtil;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private MajorMapper majorMapper;

    @Resource
    private ExcelUtil excelUtil;


    @Resource(name = "WxApiService")
    private WxApiService wxApiService;

    @Resource
    private StudentConvertor studentConvertor;
    @Override
    public SupervisorVo getSupervisorInfo(String openId) {
        return null;
    }

    @Override
    public CourseListVo getAllCourse() {
        String studentId = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        Integer currentWeek = dateUtil.getWeek(new Date(System.currentTimeMillis()));
        Integer currentDay = dateUtil.getWeekDay(new Date(System.currentTimeMillis()));

        List<CourseVo> courses = new ArrayList<>();

        boolean isOdd = currentWeek % 2 == 1;
        // 1.查出该督导员督导哪些课程
        LambdaQueryWrapper<RollRelation> crqw = new LambdaQueryWrapper<>();
        crqw.eq(RollRelation::getSupervisorId,studentId);
        List<RollRelation> rollRelations = rollRelationMapper.selectList(crqw);
        List<String> courseIds = rollRelations.stream().map(RollRelation::getCourseId).collect(Collectors.toList());

        // 2.查出该督导员当天需要点名的课程信息
        LambdaQueryWrapper<CourseArrangement> caqw = new LambdaQueryWrapper<>();
        caqw.in(CourseArrangement::getId,courseIds)
                .eq(CourseArrangement::getWeekDay,currentDay)
                .eq(CourseArrangement::getMode, isOdd ? TeachingMode.ODD_SINGLE_WEEK : TeachingMode.EVEN_SINGLE_WEEK)
                .eq(CourseArrangement::getMode,TeachingMode.EVERY_WEEK);

        List<CourseArrangement> courseArrangements = courseArrangementMapper.selectList(caqw);

        // 3.将数据封装成vo对象
        LambdaQueryWrapper<Delivery> dqw = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Course> cqw = new LambdaQueryWrapper<>();
        courseArrangements.forEach(item->{
            dqw.clear();
            cqw.clear();

            cqw.eq(Course::getId,item.getCourseId())
                    .le(Course::getStartWeek,currentWeek)
                    .ge(Course::getEndWeek,currentWeek);
            Course course = courseMapper.selectOne(cqw);
            dqw.eq(Delivery::getCourseId,item.getCourseId());
            List<Delivery> deliveries = deliveryMapper.selectList(dqw);
            List<String> professorNames = deliveries.stream().map(Delivery::getProfessorName).collect(Collectors.toList());
            CourseVo courseVo = new CourseVo();
            courseVo.setEndWeek(course.getEndWeek())
                    .setStartWeek(course.getStartWeek())
                    .setPeriod(item.getPeriod().getMsg())
                    .setId(item.getCourseId())
                    .setName(course.getCourseName())
                    .setProfessorName(StringUtils.join(professorNames,','));

            courses.add(courseVo);
        });

        return new CourseListVo(courses,courses.size());
    }

    @Override
    public void deleteCourse(String courseId) {
        courseMapper.deleteById(courseId);
        Map<String, Object> map = new HashMap<>();
        map.put("course_id",courseId);
        courseArrangementMapper.deleteByMap(map);
        deliveryMapper.deleteByMap(map);
        rollRelationMapper.deleteByMap(map);
    }

    @Override
    public CourseVo uploadCourse(CourseDTO courseDTO) {
        String supervisorId = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        //String supervisorId = "1";
        List<String> courseArrangements = courseDTO.getCourseArrangements();
        if(courseArrangements.isEmpty()){
            throw new ServiceException(ResultCode.METHOD_NOT_MATCH);
        }
        List<StudentInfo> studentInfos;
        try {
            MultipartFile studentList = courseDTO.getStudentList();
            String excelName = studentList.getOriginalFilename();
            String suffix = excelName.substring(excelName.lastIndexOf("."));
            ExcelUtil.ExcelType excelType = enumUtil.getEnumByDescription(ExcelUtil.ExcelType.class, suffix);
            studentInfos = excelUtil.importExcel(StudentInfo.class, studentList.getInputStream(), excelType);
        } catch (IOException e) {
            throw new ServiceException(ResultCode.FAILED_TO_IMPORT_EXCEL);
        }
        String courseId = org.springframework.util.StringUtils.hasText(courseDTO.getId())?courseDTO.getId():idUtil.getId();

        Course course = new Course();
        course.setId(courseId)
                .setCourseName(courseDTO.getCourseName())
                .setStartWeek(courseDTO.getStartWeek())
                .setEndWeek(courseDTO.getEndWeek())
                .setEnrollNum(studentInfos.size());
        // 插入课程
        courseMapper.insert(course);

        // 插入courseRelation
        studentInfos.forEach(info->{
            CourseRelation courseRelation = new CourseRelation()
                    .setId(idUtil.getId())
                    .setStudentId(info.getId())
                    .setCourseId(course.getId());
            courseRelationMapper.insert(courseRelation);
        });

        CourseVo courseVo = new CourseVo();
        courseVo.setId(course.getId()).setName(course.getCourseName());

        // 插入点名关系表
        rollRelationMapper.insert(new RollRelation(idUtil.getId(), supervisorId,course.getId()));

        LambdaQueryWrapper<Professor> pqw = new LambdaQueryWrapper<>();
        for (String courseArrangement:courseArrangements){
            // 拆分字符串
            String[] split = courseArrangement.split(" ");

            pqw.clear();

            String[] professorNames = courseDTO.getProfessorName().split(",");
            for(String professorName: professorNames){
                pqw.eq(Professor::getProfessorName,professorName);
                Professor professor = professorMapper.selectOne(pqw);
                if(ObjectUtils.isEmpty(professor)){
                    professor = new Professor();
                    professor.setId(idUtil.getId())
                            .setProfessorName(professorName);
                    professorMapper.insert(professor);
                    // 插入授课情况
                    Delivery delivery = new Delivery();
                    delivery.setId(idUtil.getId())
                            .setCourseId(course.getId())
                            .setProfessorName(professorName)
                            .setProfessorId(professor.getId());
                    deliveryMapper.insert(delivery);
                }
            }

            // 插入课程安排表
            CourseArrangement arrangement = new CourseArrangement();
            arrangement.setId(idUtil.getId())
                    .setCourseId(course.getId())
                    .setClassroomNo(courseDTO.getClassroomNo())
                    .setWeekDay(Integer.parseInt(split[0]))
                    .setPeriod(enumUtil.getEnumByCode(Period.class,Integer.parseInt(split[1])))
                    .setMode(enumUtil.getEnumByCode(TeachingMode.class,Integer.parseInt(split[2])));

            courseArrangementMapper.insert(arrangement);
        }

        return courseVo;
    }

    @Override
    public void updateCourse(CourseDTO courseDTO) {
        deleteCourse(courseDTO.getId());
        uploadCourse(courseDTO);
    }

    @Override
    @Transactional
    public InfoVo register(InfoDto infoDto) {
        /*获取用户的openId*/
        String openId = wxApiService.jsCode2session(infoDto.getCode()).getOpenId();

        /*检查用户是否为空*/
        if (studentMapper.selectByOpenId(openId)!=null) throw new ServiceException(ResultCode.USER_ALREADY_EXISTS);
        /*构造pojo对象*/
        Student student = studentConvertor.infoDtoToStudent(infoDto);
        /*查询院系id*/
        String departmentId = departmentMapper.selectIdByNameAndGrade(infoDto.getDepartmentName(),infoDto.getGrade());
        /*查询专业id*/
        String majorId = majorMapper.selectIdByName(infoDto.getMajorName());

        student.setOpenId(openId)
                .setRole(Role.SUPERVISOR)
                .setDepartmentId(departmentId)
                .setMajorId(majorId);

        studentMapper.insert(student);
        /*组装视图对象*/
        InfoVo infoVo = studentConvertor.studentToInfoVo(student);
        infoVo.setDepartmentName(infoDto.getDepartmentName())
                .setMajor(infoDto.getMajorName())
                .setCurrentWeek(dateUtil.getWeek(new Date(System.currentTimeMillis())));

        return infoVo;
    }

    @Data
    public static class StudentInfo{
        @Excel(value = "学号")
        private String id;

        @Excel(value = "姓名")
        private String name;
    }

}
