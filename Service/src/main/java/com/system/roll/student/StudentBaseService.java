package com.system.roll.student;

import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.student.StudentVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public interface StudentBaseService {

    /**
     * //todo 查询Student的基础信息，并封装到VO对象中
     * @param openId 学生的openId
     * @return 如果mysql查询结果为空，不需要组装VO对象，直接返回空即可
     * */
    StudentVo getStudentInfo(String openId);

    CourseListVo getAllCourse();

    InfoVo register(InfoDto infoDto);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    class InfoVo{
        private String id;
        private String name;
        private String departmentId;
        private String departmentName;
        private String majorId;
        private String major;
        private Integer grade;
        private Integer classNo;
        private Integer currentWeek;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    class InfoDto{
        private String name;
        private String departmentId;
        private String majorId;
        private Integer role;
        private String openId;
        private Integer grade;
        private Integer classNo;
    }
}
