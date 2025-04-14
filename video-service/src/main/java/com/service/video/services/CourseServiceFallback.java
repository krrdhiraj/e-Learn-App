package com.service.video.services;

import com.service.video.dtos.CourseDto;
import org.springframework.stereotype.Component;

@Component
public class CourseServiceFallback implements  CourseService{
    @Override
    public CourseDto getCourseById(String courseId) {

        CourseDto courseDto = new CourseDto();
        courseDto.setId("1234");
        courseDto.setTitle("This is dummy course");
        courseDto.setLongDesc("This is fall back called when course service is not available");
        return courseDto;
    }
}
