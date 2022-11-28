package com.system.roll.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 单次考勤数据
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RollData implements Serializable {
    /*课程号*/
    private String courseId;
    /*学生id*/
    private String studentId;
    /*学生名称*/
    private String studentName;
    /*状态*/
    private Integer state;
    /*有点名的总人数*/
    private Integer enrollNum;
    /*点名时间*/
    private Timestamp time;

    private Integer flag;//0表示统计数据，1表示单条考勤数据
}
