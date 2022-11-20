package com.system.roll;

import com.system.roll.entity.pojo.CourseArrangement;
import com.system.roll.entity.pojo.Student;
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
    void insert(){
//        CourseArrangement courseArrangement = new CourseArrangement(1L, 1L, 1, Period.EIGHT_TO_TEN, TeachingMode.EVEN_SINGLE_WEEK);
//        courseArrangementMapper.insert(courseArrangement);

        CourseArrangement courseArrangement = courseArrangementMapper.selectById("1");
        System.out.println(courseArrangement);

    }
}
