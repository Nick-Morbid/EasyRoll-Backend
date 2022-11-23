package com.system.roll.redis;

public interface CourseRedis {
    default String CourseName(){return "CourseName";}

    void saveCourseName(String courseId,String courseName);

    String getCourseName(String courseId);

    void deleteCourseName(String courseId);
}
