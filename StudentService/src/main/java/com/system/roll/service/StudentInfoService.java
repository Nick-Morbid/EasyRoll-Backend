package com.system.roll.service;

import com.system.roll.entity.vo.student.StudentVo;

public interface StudentInfoService {
    /**
     * //todo 查询Student的基础信息，并封装到VO对象中
     * @param openId 学生的openId
     * @return 如果mysql查询结果为空，不需要组装VO对象，直接返回空即可
     * */
    StudentVo getStudentInfo(String openId);

}
