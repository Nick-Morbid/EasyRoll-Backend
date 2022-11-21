package com.system.roll.service.supervisor;

import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.entity.vo.supervisor.SupervisorVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SupervisorBaseService {
    /**
     * //todo 查询Supervisor的基础信息，并封装到VO对象中
     * @param openId 督导队员的openId
     * @return 如果mysql查询结果为空，不需要组装VO对象，直接返回空即可
     * */
    SupervisorVo getSupervisorInfo(String openId);

    CourseListVo getAllCourse();

    CourseVo uploadCourse(CourseDTO courseDTO);

    void updateCourse(CourseDTO courseDTO);

    void deleteCourse(String courseId);

    InfoVo register(InfoDto infoDto);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    @ToString
    class CourseDTO{
        private String id;
        private String courseName;
        private String professorName;
        private Integer classroomNo;
        private Integer startWeek;
        private Integer endWeek;
        private Integer grade;
        private List<String> courseArrangements;
        private MultipartFile studentList;
    }

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
