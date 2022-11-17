package com.system.roll.service;

import com.system.roll.controller.SupervisorRollController;

public interface SupervisorRollService {

    /**
     * 发布定位签到
     * */
    void publishRoll(SupervisorRollController.RollDto data);


}
