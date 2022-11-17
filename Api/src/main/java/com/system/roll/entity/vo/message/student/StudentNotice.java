package com.system.roll.entity.vo.message.student;

import com.system.roll.entity.vo.message.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StudentNotice implements Message {
    private Timestamp time;
    private String content;
    private String transactor;
}
