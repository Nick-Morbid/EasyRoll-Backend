package com.system.roll.typehandler;

import com.system.roll.constant.CommonEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 这个也是自定义的枚举类映射工具
 * */
public class AutoGenericEnumTypeHandler<E extends CommonEnum> extends BaseTypeHandler<E> {

    private Class<E> enumType;
    private E[] enums;

    public AutoGenericEnumTypeHandler(){}

    public AutoGenericEnumTypeHandler(Class<E> type){
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.enumType = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(type.getName() + " does not represent an enum type.");
        }
    }

    private E loadEnum(int index) {
        for (E e : enums) {
            if (e.getCode() == index) {
                return e;
            }
        }
        throw new IllegalArgumentException(enumType.getName() + "  unknown enumerated type  index:" + index);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if(rs.getObject(columnName) == null){
            return null;
        }
        int index = rs.getInt(columnName);
        return loadEnum(index);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if(rs.getObject(columnIndex) == null){
            return null;
        }
        int index = rs.getInt(columnIndex);
        return loadEnum(index);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if(cs.getObject(columnIndex) == null){
            return null;
        }
        int index = cs.getInt(columnIndex);
        return loadEnum(index);
    }
}
