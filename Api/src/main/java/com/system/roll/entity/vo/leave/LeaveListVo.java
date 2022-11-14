package com.system.roll.entity.vo.leave;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LeaveListVo {
    private List<LeaveVo> leaveRecord;
    private Integer total;
}
