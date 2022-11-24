package com.system.roll.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
@TableName("roll_statistics")
public class RollStatistics {

    @TableId("id")
    private String id;
    private String courseId;
    private Integer enrollNum;
    private Integer absenceNum;
    private Integer attendanceNum;
    private Integer leaveNum;
    private Integer lateNum;
    private Date date;
}
