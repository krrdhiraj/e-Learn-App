package com.service.course.services;
import com.service.course.dtos.CourseDto;
import com.service.course.dtos.CustomPageResponse;
import com.service.course.dtos.ResourceContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface CourseService {

    CourseDto create(CourseDto courseDto);

    List<CourseDto> getAllCourse();

    CourseDto getCourseById(String courseId);

    CourseDto update(CourseDto courseDto, String courseId);

    ResponseEntity<?> delete(String courseId);

    List<CourseDto> searchCourses(String title);

    Page<CourseDto> getAllCoursesPages(Pageable pageable);

    CourseDto saveBanner(MultipartFile file, String courseId) throws IOException;

    ResourceContentType getCourseBannerId(String courseId);

    List<CourseDto> getCoursesOfCategory(String categoryId);
}
