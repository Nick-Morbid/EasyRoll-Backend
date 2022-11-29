package com.system.roll.entity.dto.professor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class InfoDto{
    private String id;
    private String name;
    private String departmentName;
    private String code;
    private String socketId;
    private Integer role;
}
