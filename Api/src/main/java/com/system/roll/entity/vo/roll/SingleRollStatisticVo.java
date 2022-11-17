package com.system.roll.entity.vo.roll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 对应督导队员某一次点名的统计结果
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SingleRollStatisticVo {
    private Integer enrollNum;
    private Integer attendanceNum;
    private Integer lateNum;
    private Integer absenceNum;
    private Integer leaveNum;
}
