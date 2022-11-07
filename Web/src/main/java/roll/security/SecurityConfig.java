package roll.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    //权限不足处理类
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Resource
    //认证成功处理类
    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    @Resource
    //无凭证处理类
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Resource
    //认证失败处理类
    private LoginFailureHandler loginFailureHandler;

    @Resource
    private UserDetailsService userDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // 自定义的Jwt Token过滤器
    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationTokenFilter();
    }
    //自定义的用户信息提取过滤器
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        //设置读取用户信息的页面（即登录拦截的URI）
        customAuthenticationFilter.setFilterProcessesUrl("/user/login");
        //TODO 如果要重新提取用户的过滤器类以实现json格式的数据传输，则需要将认证成功处理器和失败拦截器写到这里来！（写在configure中间无法正常工作！）
        //自定义认证成功处理器
        customAuthenticationFilter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler);
        // 自定义失败拦截器
        customAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler);
        customAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return customAuthenticationFilter;
    }


    //用来产生一个Authentication，来存放用户信息
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }





    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .formLogin()
                // 自定义登录拦截URI
                .loginProcessingUrl("/user/login")
                .and()
                //token的验证方式不需要开启csrf的防护
                .csrf().disable()
                // 自定义认证失败类
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                // 自定义权限不足处理类
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                //设置无状态的连接,即不创建session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //设置不拦截的页面
                .authorizeRequests()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/register").permitAll()
//                .antMatchers("/user/updateUserInfo").permitAll()
//                .antMatchers("/user/test").permitAll()
                .anyRequest().authenticated();

        // 解决跨域问题（重要）  只有在前端请求接口时才发现需要这个
        httpSecurity.cors().and().csrf().disable();
        //配置自己的jwt验证过滤器
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        //配置自定义的UsernamePasswordAuthenticationFilter过滤器（将登录时提取用户信息的格式改为json格式）
        httpSecurity.addFilterAt(customAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
        ;
        // disable page caching
        httpSecurity.headers().cacheControl();
    }
}
