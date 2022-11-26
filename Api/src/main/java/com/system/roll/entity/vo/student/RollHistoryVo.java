package com.system.roll.entity.vo.student;

import com.system.roll.entity.constant.impl.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RollHistoryVo {
    private String courseId;
    private String courseName;
    private Period period;
    private Integer state;
    private String supervisorId;
    private String supervisorName;
}
