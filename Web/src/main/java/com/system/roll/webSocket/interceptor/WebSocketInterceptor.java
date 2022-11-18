package com.system.roll.webSocket.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Slf4j
@SuppressWarnings("all")
@Order(1)
@WebFilter(value = "WebSocketInterceptor",urlPatterns = {"/supervisor/roll/call/*"})
public class WebSocketInterceptor  implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("发起了新的长连接");
    }
}
