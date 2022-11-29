package com.system.roll.service.supervisor;

import com.system.roll.entity.dto.student.CourseDto;
import com.system.roll.entity.dto.student.InfoDto;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.entity.vo.student.InfoVo;
import com.system.roll.entity.vo.supervisor.SupervisorVo;

public interface SupervisorBaseService {
    /**
     * //todo 查询Supervisor的基础信息，并封装到VO对象中
     * @param openId 督导队员的openId
     * @return 如果mysql查询结果为空，不需要组装VO对象，直接返回空即可
     * */
    SupervisorVo getSupervisorInfo(String openId);

    CourseListVo getAllCourse();

    CourseVo uploadCourse(CourseDto courseDto);

    CourseVo updateCourse(CourseDto courseDto);

    void deleteCourse(String courseId);

    InfoVo register(InfoDto infoDto);

}
