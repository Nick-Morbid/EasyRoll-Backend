package roll.security;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.entity.vo.Result;
import com.system.roll.utils.JsonUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//认证失败处理类
/*当用户输入错误的账号或者密码时，就会进入这个处理类，同样要在配置类中指明
* */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        Result<?> result = Result.error(ResultCode.LOGIN_FAILURE);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JsonUtil.toJson(result));
    }
}
