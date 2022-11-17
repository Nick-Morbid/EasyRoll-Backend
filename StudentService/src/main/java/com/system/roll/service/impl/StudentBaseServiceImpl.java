package com.system.roll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.constant.impl.TeachingMode;
import com.system.roll.entity.pojo.Course;
import com.system.roll.entity.pojo.CourseArrangement;
import com.system.roll.entity.pojo.CourseRelation;
import com.system.roll.entity.pojo.Delivery;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.mapper.CourseArrangementMapper;
import com.system.roll.mapper.CourseMapper;
import com.system.roll.mapper.CourseRelationMapper;
import com.system.roll.mapper.DeliveryMapper;
import com.system.roll.security.context.SecurityContextHolder;
import com.system.roll.service.StudentBaseService;
import com.system.roll.utils.DateUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component(value = "studentBaseService")
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
                .lt(CourseArrangement::getStartWeek,currentWeek)
                .ge(CourseArrangement::getEndWeek,currentWeek)
                .eq(CourseArrangement::getWeekDay,currentDay)
                .eq(CourseArrangement::getMode, isOdd ? TeachingMode.ODD_SINGLE_WEEK : TeachingMode.EVEN_SINGLE_WEEK)
                .eq(CourseArrangement::getMode,TeachingMode.EVERY_WEEK);

        List<CourseArrangement> courseArrangements = courseArrangementMapper.selectList(caqw);

        // 3.将数据封装成vo对象
        LambdaQueryWrapper<Delivery> dqw = new LambdaQueryWrapper<>();
        courseArrangements.forEach(item->{
            dqw.clear();
            Course course = courseMapper.selectById(item.getCourseId());
            dqw.eq(Delivery::getCourseId,item.getCourseId());
            List<Delivery> deliveries = deliveryMapper.selectList(dqw);
            List<String> professorNames = deliveries.stream().map(Delivery::getProfessorName).collect(Collectors.toList());
            CourseVo courseVo = new CourseVo();
            courseVo.setEndWeek(item.getEndWeek())
                    .setStartWeek(item.getStartWeek())
                    .setPeriod(item.getPeriod().getMsg())
                    .setId(item.getCourseId())
                    .setName(course.getCourseName())
                    .setProfessorName(StringUtils.join(professorNames,','));

            courses.add(courseVo);
        });

        return new CourseListVo(courses,courses.size());
    }
}
