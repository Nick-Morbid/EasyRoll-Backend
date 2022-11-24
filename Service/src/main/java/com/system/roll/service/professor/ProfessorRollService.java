package com.system.roll.service.professor;

import com.system.roll.entity.vo.roll.RollDataVo;
import com.system.roll.entity.vo.roll.TotalRollStatisticVo;
import com.system.roll.entity.vo.student.StudentRollDataListVo;
import com.system.roll.entity.vo.student.StudentRollRecord;

public interface ProfessorRollService {
    RollDataVo getRollData(Long courseId);

    StudentRollRecord getOneClassMember(Long courseId, Long studentId);

    TotalRollStatisticVo getStatistic(Long courseId, Integer sortRole);

    StudentRollDataListVo getClassMembers(Long courseId, Integer sortRule);
}
