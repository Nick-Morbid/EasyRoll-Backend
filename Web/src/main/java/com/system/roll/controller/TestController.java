package com.system.roll.controller;

import com.system.roll.constant.impl.Period;
import com.system.roll.constant.impl.Role;
import com.system.roll.entity.pojo.User;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.security.context.SecurityContextHolder;
import com.system.roll.security.jwt.JwtSecurityHandler;
import com.system.roll.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/test")
@Slf4j
public class TestController {
    @GetMapping("/test01")
    public User test01(){
        return new User().setId("1").setName("nick").setPassword("123");
    }

    @Resource(name = "UserService")
    private UserService userService;

    @GetMapping("/test02")
    public CourseListVo test02(){

        System.out.println(SecurityContextHolder.getContext().getAuthorization().getInfo(Long.class, "id"));
        System.out.println(SecurityContextHolder.getContext().getAuthorization().getInfo(Role.class, "role"));
        System.out.println(SecurityContextHolder.getContext().getAuthorization().getInfo(Long.class, "departmentId"));

        List<CourseListVo.CourseVo> courses = new ArrayList<>();
        courses.add(new CourseListVo.CourseVo().setName("软件工程").setProfessorName("Kex").setStartWeek(2).setEndWeek(14).setId(1L).setEnrollNum(139).setPeriod(Period.EIGHT_TO_TEN.getMsg()));
        return new CourseListVo().setCourses(courses).setTotal(courses.size());
    }

    @Resource
    private JwtSecurityHandler jwtSecurityHandler;

    @GetMapping(value = "/login")
    public User login(HttpServletResponse response) throws IOException {
        String token = jwtSecurityHandler.getToken(32002601L,0,100000001L);
        response.setHeader("Authorization",token);
        return new User().setName("nick");
    }
}
