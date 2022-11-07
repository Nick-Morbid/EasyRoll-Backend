package roll.security;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.entity.vo.Result;
import com.system.roll.utils.JsonUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//无凭证处理类
/*当用户没有携带有效凭证时，就会转到这里来，当然，我们还需要在Spring Security的配置类中指定我们自定义的处理类才可以
 * */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        Result<?> result = Result.error(ResultCode.NOT_ACCESS);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JsonUtil.toJson(result));
    }
}
