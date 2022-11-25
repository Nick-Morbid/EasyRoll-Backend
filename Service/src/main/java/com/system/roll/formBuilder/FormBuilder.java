package com.system.roll.formBuilder;

import com.system.roll.entity.vo.student.StudentRollListVo;

public interface FormBuilder {
    /**
     * 根据学生id获取点名表单
     * */
    StudentRollListVo getForm(String courseId);
}
