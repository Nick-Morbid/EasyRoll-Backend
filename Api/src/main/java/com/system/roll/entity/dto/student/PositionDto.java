package com.system.roll.entity.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PositionDto {
    /*课程id*/
    private String courseId;
    /*经度*/
    private Double longitude;
    /*维度*/
    private Double dimension;
}
