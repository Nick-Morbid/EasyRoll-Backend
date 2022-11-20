package com.system.roll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.constant.impl.Period;
import com.system.roll.constant.impl.ResultCode;
import com.system.roll.constant.impl.TeachingMode;
import com.system.roll.controller.SupervisorBaseController;
import com.system.roll.entity.pojo.*;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.exception.impl.ServiceException;
import com.system.roll.mapper.*;
import com.system.roll.security.context.SecurityContextHolder;
import com.system.roll.service.SupervisorBaseService;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.EnumUtil;
import com.system.roll.utils.IdUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(value = "supervisorBaseService")
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
    public CourseVo uploadCourse(SupervisorBaseController.CourseDTO courseDTO) {
        String supervisorId = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");

        // TODO:插入课程关系表
        List<SupervisorBaseController.CourseArrangement> courseArrangements = courseDTO.getCourseArrangements();
        if(courseArrangements.isEmpty()){
            throw new ServiceException(ResultCode.METHOD_NOT_MATCH);
        }

        Course course = new Course();
        // TODO: setEnrollNum、setTotal
        course.setId(idUtil.getId())
                .setCourseName(courseDTO.getCourseName())
                .setStartWeek(courseDTO.getStartWeek())
                .setEndWeek(courseDTO.getEndWeek());
        // 插入课程
        courseMapper.insert(course);

        CourseVo courseVo = new CourseVo();
        courseVo.setId(course.getId()).setName(course.getCourseName());

        LambdaQueryWrapper<Professor> pqw = new LambdaQueryWrapper<>();
        courseArrangements.forEach(courseArrangement -> {
            pqw.clear();
            // 插入点名关系表
            rollRelationMapper.insert(new RollRelation(idUtil.getId(), supervisorId,course.getId()));

            String[] professorNames = org.springframework.util.StringUtils.split(courseDTO.getCourseName(), ",");
            if (professorNames != null) {
                for(String professorName: professorNames){
                    Professor professor = professorMapper.selectOne(pqw);
                    if(ObjectUtils.isEmpty(professor)){
                        professor = new Professor();
                        professor.setId(idUtil.getId())
                                .setProfessorName(professorName);
                        professorMapper.insert(professor);
                    }

                    // 插入授课情况
                    Delivery delivery = new Delivery();
                    delivery.setId(idUtil.getId())
                            .setCourseId(course.getId())
                            .setProfessorName(professorName)
                            .setProfessorId(professor.getId());
                    deliveryMapper.insert(delivery);
                }

                // 插入课程安排表
                CourseArrangement arrangement = new CourseArrangement();
                arrangement.setId(idUtil.getId())
                        .setCourseId(course.getId())
                        .setClassroomNo(courseDTO.getClassroomNo())
                        .setWeekDay(courseArrangement.getWeekDay())
                        .setPeriod(enumUtil.getEnumByCode(Period.class,courseArrangement.getPeriod()))
                        .setMode(enumUtil.getEnumByCode(TeachingMode.class,courseArrangement.getMode()));

                courseArrangementMapper.insert(arrangement);
            }else{
                throw new ServiceException(ResultCode.METHOD_NOT_MATCH);
            }
        });

        return courseVo;
    }

    @Override
    public SupervisorBaseController.InfoVo register(SupervisorBaseController.InfoDto infoDto) {
        Student student = new Student(idUtil.getId(),
                infoDto.getName(),
                infoDto.getDepartmentId(),
                infoDto.getMajorId(),
                infoDto.getGrade(),
                infoDto.getClassNo(),
                infoDto.getOpenId(),
                infoDto.getRole());

        studentMapper.insert(student);
        Department department = departmentMapper.selectById(infoDto.getDepartmentId());
        Major major = majorMapper.selectById(infoDto.getMajorId());

        SupervisorBaseController.InfoVo infoVo = new SupervisorBaseController.InfoVo(
                student.getId(),
                student.getStudentName(),
                student.getDepartmentId(),
                department.getDepartmentName(),
                student.getMajorId(),
                major.getMajorName(),
                student.getGrade(),
                student.getClassNo(),
                dateUtil.getWeek(new Date(System.currentTimeMillis())));

        return infoVo;
    }
}
