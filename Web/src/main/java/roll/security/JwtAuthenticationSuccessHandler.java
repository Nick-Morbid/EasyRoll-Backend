package roll.security;

import com.system.roll.entity.pojo.User;
import com.system.roll.entity.vo.Result;
import com.system.roll.entity.vo.UserVo;
import com.system.roll.utils.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import roll.properties.JwtProperties;
import roll.service.UserService;
import roll.utils.JwtTokenUtil;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* 认证成功处理器，在这里生成token并放入response头中！
* */
@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

    }
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private JwtProperties jwtProperties;
    @Resource(name = "UserService")
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        String userId = authentication.getName();
        User user = userService.getById(userId);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        Result<?> result = Result.success(userVo);

        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JsonUtil.toJson(result));

        String token = jwtTokenUtil.generateToken(userId);
        httpServletResponse.setHeader(jwtProperties.getHeader(),token);
        //将生成的authentication放入容器中，生成安全的上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
