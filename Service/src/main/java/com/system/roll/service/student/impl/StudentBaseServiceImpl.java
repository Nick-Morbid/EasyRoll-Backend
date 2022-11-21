package com.system.roll.service.student.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.entity.constant.impl.Role;
import com.system.roll.entity.constant.impl.TeachingMode;
import com.system.roll.entity.pojo.*;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.entity.vo.student.StudentVo;
import com.system.roll.mapper.*;
import com.system.roll.context.security.SecurityContextHolder;
import com.system.roll.service.student.StudentBaseService;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.EnumUtil;
import com.system.roll.utils.IdUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Component;

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

    @Override
    public StudentVo getStudentInfo(String openId) {
        return null;
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
                    .setProfessorName(StringUtils.join(professorNames,','));

            courses.add(courseVo);
        });

        return new CourseListVo(courses,courses.size());
    }

    @Override
    public InfoVo register(InfoDto infoDto) {
        Student student = new Student(idUtil.getId(),
                infoDto.getName(),
                infoDto.getDepartmentId(),
                infoDto.getMajorId(),
                infoDto.getGrade(),
                infoDto.getClassNo(),
                infoDto.getOpenId(),
                enumUtil.getEnumByCode(Role.class,infoDto.getRole())
                );

        studentMapper.insert(student);
        Department department = departmentMapper.selectById(infoDto.getDepartmentId());
        Major major = majorMapper.selectById(infoDto.getMajorId());

        return new InfoVo(
                student.getId(),
                student.getStudentName(),
                student.getDepartmentId(),
                department.getDepartmentName(),
                student.getMajorId(),
                major.getMajorName(),
                student.getGrade(),
                student.getClassNo(),
                dateUtil.getWeek(new Date(System.currentTimeMillis())));
    }
}
