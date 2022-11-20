package com.system.roll;

import com.system.roll.entity.pojo.CourseArrangement;
import com.system.roll.mapper.CourseArrangementMapper;
import com.system.roll.mapper.StudentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MapperTest {

    @Resource
    StudentMapper studentMapper;

    @Resource
    CourseArrangementMapper courseArrangementMapper;

    @Test
    void testInsert(){
//        Student student = new Student("1","陈宏侨","1","1",2020,6);
//        studentMapper.insert(student);
    }

    @Test
    void insert(){
//        CourseArrangement courseArrangement = new CourseArrangement(1L, 1L, 1, Period.EIGHT_TO_TEN, TeachingMode.EVEN_SINGLE_WEEK);
//        courseArrangementMapper.insert(courseArrangement);

        CourseArrangement courseArrangement = courseArrangementMapper.selectById(1L);
        System.out.println(courseArrangement);

    }
}
