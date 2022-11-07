package roll.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.system.roll.constant.impl.ResultCode;
import com.system.roll.entity.pojo.User;
import com.system.roll.exception.impl.AuthException;
import com.system.roll.mapper.UserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import roll.entity.bo.UserBo;
import roll.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service(value = "UserService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserBo userBo = new UserBo();
        User user = getById(s);
        if (user!=null){
            List<GrantedAuthority> list = new ArrayList<>();
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_"+user.getRole().name());
            list.add(grantedAuthority);
            userBo.setUsername(user.getId()).setPassword(user.getPassword()).setAuthorities(list);
        }else {
            throw new AuthException(ResultCode.NOT_FIND_USER);
        }
        return userBo;
    }
}




