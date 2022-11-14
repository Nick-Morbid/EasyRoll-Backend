package com.system.roll.service;

import com.system.roll.entity.vo.supervisor.SupervisorVo;

public interface SupervisorInfoService {
    /**
     * //todo 查询Supervisor的基础信息，并封装到VO对象中
     * @param openId 督导队员的openId
     * @return 如果mysql查询结果为空，不需要组装VO对象，直接返回空即可
     * */
    SupervisorVo getSupervisorInfo(String openId);
}
