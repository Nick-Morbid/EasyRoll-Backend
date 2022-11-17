package com.system.roll.entity.vo.message;

import java.sql.Timestamp;

public interface Message {
    Message setTime(Timestamp time);
    Message setContent(String content);

}
