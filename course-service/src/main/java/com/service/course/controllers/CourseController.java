package com.service.course.controllers;

import com.service.course.config.AppConstants;
import com.service.course.dtos.CourseDto;
import com.service.course.dtos.CustomMessage;
import com.service.course.dtos.CustomPageResponse;
import com.service.course.dtos.ResourceContentType;
import com.service.course.entities.Course;
import com.service.course.services.CourseService;
import com.service.course.services.FileService;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@CrossOrigin("http://localhost:4200")
public class CourseController {


    private final ModelMapper modelMapper;

    private final CourseService courseService;

    public CourseController(ModelMapper modelMapper, CourseService courseService, FileService fileService) {
        this.modelMapper = modelMapper;
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(
            @RequestBody CourseDto courseDto
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(courseDto));
    }

    @Retry(name = "getSingleCourseRetry", fallbackMethod = "singleCourseRetryFallback")
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDto> getSingle(
            @PathVariable String courseId
    ){
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }
    public ResponseEntity<CourseDto> singleCourseRetryFallback(Throwable ex){
        CourseDto courseDto = new CourseDto();
        courseDto.setTitle("Spring-boot latest course");
        courseDto.setShortDesc("This is very latest course of Spring boot");
        courseDto.setLongDesc("This is very latest course of Spring boot running by Dhiraj Singh.");
        return ResponseEntity.ok(courseDto);
    }
    @GetMapping
    public List<Course> getAll(){
        return courseService.getAllCourse().stream().map(courseDto ->
                modelMapper.map(courseDto,Course.class)).toList();
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseDto> updateCourse(
            @PathVariable String courseId,
            @RequestBody CourseDto courseDto

    ){
        CourseDto update = courseService.update(courseDto, courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(update);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(
            @PathVariable String courseId
    ){
        courseService.delete(courseId);
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully😊.");
    }

    @GetMapping("/pages")
    public Page<CourseDto> getAllpages(Pageable pageable){
        return  courseService.getAllCoursesPages(pageable);
    }
    @GetMapping("/search")
    public ResponseEntity<List<CourseDto>> searchCourses(
            @RequestParam String keyword){
        return ResponseEntity.ok(courseService.searchCourses(keyword));
    }

    // banner upload api
    @PostMapping("/{courseId}/banner")
    public ResponseEntity<?> uploadBanner(
            @PathVariable String courseId,
            @RequestParam("banner") MultipartFile banner
    ) throws IOException {

        // validate the file
        String contentType = banner.getContentType();

        if(contentType == null){
            contentType = "image/png";
        }else if(contentType.equalsIgnoreCase("image/png") || contentType.equalsIgnoreCase("image/jpg")){

        }
        else{
                CustomMessage customMessage = new CustomMessage();
                customMessage.setStatus(HttpStatus.BAD_REQUEST);
                customMessage.setSuccess(false);
                customMessage.setMessage("Invalid file");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customMessage);
            }
        System.out.println(banner.getOriginalFilename());
        System.out.println(banner.getName());
        System.out.println(banner.getSize());
        System.out.println(banner.getContentType());

        CourseDto courseDto = courseService.saveBanner(banner, courseId);
        return ResponseEntity.ok(courseDto);
    }
    // serve banner
    @GetMapping("/{courseId}/banners")
    public ResponseEntity<Resource> serveBanner(
            @PathVariable String courseId
//            @RequestHeader("Content-Type") String contentType,
//            HttpServletRequest httpServletRequest,
//            HttpServletResponse httpServletResponse,
//            HttpSession httpSession,
//            ServletContext servletContext
    ){

//        System.out.println(request.getContextPath());
//        System.out.println(request.getPathInfo());

//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements())
//        {
//            String header = headerNames.nextElement();
//            System.out.println( header+" : "+request.getHeader(header));
//        }

        ResourceContentType resourceContentType = courseService.getCourseBannerId(courseId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resourceContentType.getContentType()))
                .body(resourceContentType.getResource());
    }

    //get all courses of category
    //api/v1/courses/category/35235235
    @GetMapping("/category/{categoryId}")
    public List<CourseDto> getCourseOfCategory(@PathVariable String categoryId){
        return courseService.getCoursesOfCategory(categoryId);
    }
}
