package com.system.roll.entity.pojo;

import com.system.roll.entity.constant.impl.RollDataType;
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
    /*考勤数据类型*/
    private RollDataType flag;
}
