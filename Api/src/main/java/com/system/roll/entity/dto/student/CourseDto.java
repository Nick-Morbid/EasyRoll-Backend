package com.system.roll.entity.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class CourseDto{
    private String id;
    private String courseName;
    private String professorName;
    private Integer classroomNo;
    private Integer startWeek;
    private Integer endWeek;
    private Integer grade;
    private List<String> courseArrangements;
    private MultipartFile studentList;
}
