package com.system.roll.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "position")
public class Position {
    @TableId(value = "position_id")
    private String positionId;
    @TableField(value = "dorm_no")
    private Integer dormNo;
    @TableField(value = "point1_longitude")
    private Double point1Longitude;
    @TableField(value = "point1_dimension")
    private Double point1Dimension;
    @TableField(value = "point2_longitude")
    private Double point2Longitude;
    @TableField(value = "point2_dimension")
    private Double point2Dimension;
    @TableField(value = "point3_longitude")
    private Double point3Longitude;
    @TableField(value = "point3_dimension")
    private Double point3Dimension;
    @TableField(value = "point4_longitude")
    private Double point4Longitude;
    @TableField(value = "point4_dimension")
    private Double point4Dimension;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Point{
        public Double x;
        public Double y;
    }

}
