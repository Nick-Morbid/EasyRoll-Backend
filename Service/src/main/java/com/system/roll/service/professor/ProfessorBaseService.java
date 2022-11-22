package com.system.roll.service.professor;

import com.system.roll.entity.dto.professor.InfoDto;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.professor.InfoVo;
import com.system.roll.entity.vo.professor.ProfessorVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

public interface ProfessorBaseService {

    /**
     * //todo 查询Professor的基础信息，并封装到VO对象中
     * @param openId Professor的openId
     * @return 如果mysql查询结果为空，不需要组装VO对象，直接返回空即可
     * */
    ProfessorVo getProfessorInfo(String openId);

    CourseListVo getCourseList(String courseId);

    InfoVo register(InfoDto infoDto);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    class CourseArrangementDto{
        private Integer weekDay;
        public Integer period;
        private Integer mode;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    class CourseDto{
        private String courseName;
        private String professorName;
        private Integer classroomNo;
        private Integer startWeek;
        private Integer endWeek;
        private Integer grade;
        private List<CourseArrangementDto> courseArrangements;
    }
}
