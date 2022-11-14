package com.system.roll.service.professor;

import com.system.roll.entity.vo.professor.ProfessorVo;

public interface ProfessorInfoService {
    /**
     * //todo 查询Professor的基础信息，并封装到VO对象中
     * @param openId Professor的openId
     * @return 如果mysql查询结果为空，不需要组装VO对象，直接返回空即可
     * */
    ProfessorVo getProfessorInfo(String openId);
}
