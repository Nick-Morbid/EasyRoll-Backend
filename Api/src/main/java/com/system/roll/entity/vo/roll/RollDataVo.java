package com.system.roll.entity.vo.roll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class RollDataVo {

    private String courseId;
    private String courseName;
    private Integer enrollNum = 0;
    private Integer currentNum = 0;
    private Integer attendanceNum = 0;
    private  Integer absenceNum = 0;
    private Integer leaveNum = 0;
    private Integer lateNum = 0;
    private Date date;
    private List<Record> absenceList;
    private List<Record> leaveList;
    private List<Record> lateList;

    public RollDataVo(){
        this.absenceList = new ArrayList<>();
        this.leaveList = new ArrayList<>();
        this.lateList = new ArrayList<>();
    }

    public void incrAttendanceNum() {
        this.attendanceNum++;
    }

    public void incrAbsenceNum() {
        this.absenceNum++;
    }

    public void incrLateNum() {
        this.lateNum++;
    }

    public void incrLeaveNum() {
        this.leaveNum++;
    }

    public void addAbsence(String studentId,String studentName){
        incrAbsenceNum();
        this.absenceList.add(new Record().setStudentId(studentId).setStudentName(studentName));
    }

    public void addLate(String studentId,String studentName){
        incrLateNum();
        incrAttendanceNum();
        this.lateList.add(new Record().setStudentId(studentId).setStudentName(studentName));
    }

    public void addLeave(String studentId,String studentName){
        incrLeaveNum();
        this.leaveList.add(new Record().setStudentId(studentId).setStudentName(studentName));
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Record{
        private String studentId;
        private String studentName;
    }
}
