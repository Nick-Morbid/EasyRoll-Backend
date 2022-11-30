package com.system.roll.utils;

import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.pojo.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "PositionUtil")
public class PositionUtil {

    /**
     * 经纬度转换为十进制坐标
     * */
    public Double toDecimal(String position){
        String[] split = position.split("\\.");
        if (split.length!=3)throw new ServiceException(ResultCode.PARAM_NOT_MATCH);
        double degrees = Double.parseDouble(split[0]);
        double minutes = Double.parseDouble(split[1]);
        double seconds = Double.parseDouble(split[2]);
        return degrees+minutes/60+seconds/3600;
    }

    public Boolean isInside(Position.Point p, Position position){
        Position.Point a = new Position.Point(position.getPoint1Longitude(), position.getPoint1Dimension());
        Position.Point b = new Position.Point(position.getPoint2Longitude(), position.getPoint2Dimension());
        Position.Point c = new Position.Point(position.getPoint3Longitude(), position.getPoint3Dimension());
        Position.Point d = new Position.Point(position.getPoint4Longitude(), position.getPoint4Dimension());

        double dTriangle = triangleArea(a, b, p) + triangleArea(b, c, p)
                + triangleArea(c, d, p) + triangleArea(d, a, p);
        double dQuadrangle = triangleArea(a, b, c) + triangleArea(c, d, a);
        return Math.abs(dTriangle-dQuadrangle)<=1.0E-10;
    }

    private double triangleArea(Position.Point a, Position.Point b, Position.Point c) {
        return Math.abs((a.x * b.y + b.x * c.y + c.x * a.y - b.x * a.y
                - c.x * b.y - a.x * c.y) / 2.0D);
    }
}
