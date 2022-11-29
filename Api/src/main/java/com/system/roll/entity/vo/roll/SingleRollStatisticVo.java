package com.system.roll.entity.vo.roll;

import com.system.roll.entity.pojo.RollData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Date;

/**
 * 对应督导队员某一次点名的统计结果
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SingleRollStatisticVo {
    private Integer enrollNum = 0;
    private Integer attendanceNum = 0;
    private Integer lateNum = 0;
    private Integer absenceNum = 0;
    private Integer leaveNum = 0;
    private Date date;//考勤时间
    private RollData rollData;//最近一次的考勤数据（考勤还未完成时有效）

    public void incrAbsenceNum(){
        this.absenceNum++;
    }
    public void incrAttendanceNum(){
        this.attendanceNum++;
    }
    public void incrLateNum(){
        this.lateNum++;
    }
    public void incrLeaveNum(){
        this.leaveNum++;
    }
}
