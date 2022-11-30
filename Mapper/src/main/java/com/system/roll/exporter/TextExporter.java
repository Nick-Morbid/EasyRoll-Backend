package com.system.roll.exporter;

import com.system.roll.entity.constant.impl.Period;

import java.sql.Date;

public interface TextExporter {
    /**
     * 导出文本数据
     * */
    String outputText(String courseId, Date date, Period period);
}
