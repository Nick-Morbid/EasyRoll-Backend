package com.system.roll.service.supervisor;

import com.system.roll.entity.vo.message.MessageListVo;
import com.system.roll.entity.vo.roll.SingleRollStatisticVo;
import com.system.roll.entity.vo.student.StudentRollListVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

public interface SupervisorRollService {

    /**
     * 发布定位签到
     * */
    void publishRoll(RollDto data);

    /**
     * 获取点名表单
     * */
    StudentRollListVo getForm(String courseId);

    /**
     * 获取单次点名的统计结果
     * */
    SingleRollStatisticVo getRollDataStatistic(String courseId) throws InterruptedException;

    /**
     * 获取消息列表
     * */
    MessageListVo getMessageList();

    /**
     * 处理申诉消息
     * */
    void solveMessage(SolveDto data);

    /**
     * 导出为excel
     * */
    void outputExcel(String courseId);

    /**
     * 导出为text
     * */
    void outputText(Long courseId);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    class RollDto{
        private Timestamp startTime;
        private Timestamp endTime;
        private String courseId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    class SolveDto{
        private String id;
        private Integer msgType;
        private Integer result;
    }

}
