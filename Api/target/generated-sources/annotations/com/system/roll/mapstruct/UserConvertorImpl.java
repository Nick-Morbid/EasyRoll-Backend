package com.system.roll.mapstruct;

import com.system.roll.constant.impl.Role;
import com.system.roll.entity.pojo.User;
import com.system.roll.entity.vo.UserVo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-14T17:04:17+0800",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.17 (Amazon.com Inc.)"
)
@Component
public class UserConvertorImpl implements UserConvertor {

    @Override
    public UserVo UserToUserVo(User user) {
        if ( user == null ) {
            return null;
        }

        UserVo userVo = new UserVo();

        userVo.setUserId( user.getId() );
        userVo.setUserName( user.getName() );
        userVo.setRoleName( userRoleMsg( user ) );

        return userVo;
    }

    private String userRoleMsg(User user) {
        if ( user == null ) {
            return null;
        }
        Role role = user.getRole();
        if ( role == null ) {
            return null;
        }
        String msg = role.getMsg();
        if ( msg == null ) {
            return null;
        }
        return msg;
    }
}
