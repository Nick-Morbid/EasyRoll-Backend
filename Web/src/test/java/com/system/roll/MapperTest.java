package com.system.roll;

import com.system.roll.constant.impl.Period;
import com.system.roll.constant.impl.TeachingMode;
import com.system.roll.entity.pojo.CourseArrangement;
import com.system.roll.entity.pojo.Student;
import com.system.roll.mapper.CourseArrangementMapper;
import com.system.roll.mapper.StudentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapperTest {

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    CourseArrangementMapper courseArrangementMapper;

    @Test
    void testInsert(){
        Student student = new Student(1L,"陈宏侨",1L,1L,2020,6);
        studentMapper.insert(student);
    }

    @Test
    void insert(){
//        CourseArrangement courseArrangement = new CourseArrangement(1L, 1L, 1, Period.EIGHT_TO_TEN, TeachingMode.EVEN_SINGLE_WEEK);
//        courseArrangementMapper.insert(courseArrangement);

        CourseArrangement courseArrangement = courseArrangementMapper.selectById(1L);
        System.out.println(courseArrangement);

    }
}