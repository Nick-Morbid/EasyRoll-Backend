package com.system.roll.service.student.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.context.security.SecurityContextHolder;
import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.constant.impl.Role;
import com.system.roll.entity.constant.impl.TeachingMode;
import com.system.roll.entity.dto.student.InfoDto;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.pojo.*;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.entity.vo.student.InfoVo;
import com.system.roll.handler.mapstruct.StudentConvertor;
import com.system.roll.mapper.*;
import com.system.roll.redis.StudentRedis;
import com.system.roll.service.auth.WxApiService;
import com.system.roll.service.student.StudentBaseService;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.EnumUtil;
import com.system.roll.utils.IdUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component(value = "StudentBaseService")
public class StudentBaseServiceImpl implements StudentBaseService {
    @Resource
    private DateUtil dateUtil;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private CourseArrangementMapper courseArrangementMapper;

    @Resource
    private CourseRelationMapper courseRelationMapper;

    @Resource
    private DeliveryMapper deliveryMapper;

    @Resource
    private IdUtil idUtil;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private MajorMapper majorMapper;

    @Resource
    private EnumUtil enumUtil;

    @Resource(name = "WxApiService")
    private WxApiService wxApiService;

    @Resource
    private StudentConvertor studentConvertor;

    @Resource
    private StudentRedis studentRedis;

    @Override
    public InfoVo getStudentInfo(String openId) {
        Student student = studentMapper.selectByOpenId(openId);
        if (student==null) return null;
        InfoVo infoVo = studentConvertor.studentToInfoVo(student);
        String departmentName = departmentMapper.selectNameById(student.getDepartmentId());
        String majorName = majorMapper.selectNameById(student.getMajorId());
        return infoVo.setDepartmentName(departmentName).setMajorName(majorName).setCurrentWeek(dateUtil.getWeek(new Date(System.currentTimeMillis())));
    }

    @Override
    public CourseListVo getAllCourse() {
        String studentId = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        Integer currentWeek = dateUtil.getWeek(new Date(System.currentTimeMillis()));
        Integer currentDay = dateUtil.getWeekDay(new Date(System.currentTimeMillis()));

        List<CourseVo> courses = new ArrayList<>();

        boolean isOdd = currentWeek % 2 == 1;
        // 1.查出该学生选了哪些课程
        LambdaQueryWrapper<CourseRelation> crqw = new LambdaQueryWrapper<>();
        crqw.eq(CourseRelation::getStudentId,studentId);
        List<CourseRelation> courseRelations = courseRelationMapper.selectList(crqw);
        List<String> courseIds = courseRelations.stream().map(CourseRelation::getCourseId).collect(Collectors.toList());

        // 2.查出该学生所选的课程中，当天需要点名的课程信息
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
                    .setProfessorName(StringUtils.join(professorNames,','))
                    .setEnrollNum(course.getEnrollNum());

            courses.add(courseVo);
        });

        return new CourseListVo(courses,courses.size());
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
                .setRole(Role.STUDENT)
                .setDepartmentId(departmentId)
                .setMajorId(majorId);

        studentRedis.saveName(student.getId(),student.getStudentName());
        studentMapper.insert(student);
        /*组装视图对象*/
        InfoVo infoVo = studentConvertor.studentToInfoVo(student);
        infoVo.setDepartmentName(infoDto.getDepartmentName())
                .setMajorName(infoDto.getMajorName())
                .setCurrentWeek(dateUtil.getWeek(new Date(System.currentTimeMillis())));

        return infoVo;
    }
}
