package com.system.roll.service.impl;

import com.system.roll.controller.SupervisorRollController;
import com.system.roll.service.SupervisorRollService;
import org.springframework.stereotype.Component;

@Component(value = "supervisorRollService")
public class SupervisorRollServiceImpl implements SupervisorRollService {
    @Override
    public void publishRoll(SupervisorRollController.RollDto data) {

    }
}
