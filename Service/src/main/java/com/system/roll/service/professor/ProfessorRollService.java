package com.system.roll.service.professor;

import com.system.roll.entity.vo.roll.RollDataVo;
import com.system.roll.entity.vo.roll.TotalRollStatisticVo;
import com.system.roll.entity.vo.student.StudentRollDataListVo;
import com.system.roll.entity.vo.student.StudentRollRecord;

public interface ProfessorRollService {
    RollDataVo getRollData(String courseId);

    StudentRollRecord getOneClassMember(String courseId, String studentId);

    TotalRollStatisticVo getStatistic(String courseId, Integer sortRole);

    StudentRollDataListVo getClassMembers(String courseId, Integer sortRule);
}
