package com.system.roll.service.professor;

import com.system.roll.entity.vo.roll.RollDataVo;
import com.system.roll.entity.vo.roll.TotalRollStatisticVo;
import com.system.roll.entity.vo.student.StudentRollRecord;

public interface ProfessorRollService {
    RollDataVo getRollData(Long courseId);

    StudentRollRecord getClassMembers(Long courseId, Long studentId);

    TotalRollStatisticVo getStatistic(Long courseId, Integer sortRole);
}
