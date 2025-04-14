package com.service.course.servicesImpl;

import com.service.course.config.AppConstants;
import com.service.course.dtos.*;
import com.service.course.entities.Course;
import com.service.course.exceptions.ResourceNotFoundException;
import com.service.course.repositories.CourseRepo;
import com.service.course.services.CourseService;
import com.service.course.services.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final RestTemplate restTemplate;
    private final WebClient.Builder webClient;

    public CourseServiceImpl(CourseRepo courseRepo, ModelMapper modelMapper, FileService fileService,
                             RestTemplate restTemplate, WebClient.Builder webClient) {
        this.courseRepo = courseRepo;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
        this.restTemplate = restTemplate;
        this.webClient = webClient;
    }

    @Override
    public CourseDto create(CourseDto courseDto) {
        courseDto.setId(UUID.randomUUID().toString());
        courseDto.setCreatedDate(new Date());
        Course savedCourse = courseRepo.save(modelMapper.map(courseDto,Course.class));
        return modelMapper.map(savedCourse,CourseDto.class);
    }

    @Override
    public List<CourseDto> getAllCourse() {
        List<Course> courseList = courseRepo.findAll();
        return courseList
                .stream()
                .map(course->modelMapper.map(course , CourseDto.class)).collect(toList());
    }

    @Override
    public CourseDto getCourseById(String courseId) {
        Course course = courseRepo.findById(courseId).orElseThrow(() ->
                new ResourceNotFoundException("No course found with this Id🫣, Pls try id"));

        CourseDto courseDto = modelMapper.map(course, CourseDto.class);

        //load category of the course [by calling Category Service]

//        CategoryDto forObject = restTemplate.getForObject(
//                AppConstants.DEFAULT_CATEGORY_URL + "/categories/" + course.getCategoryId(), CategoryDto.class);
        courseDto.setCategoryDto(getCategoryOfCourse(courseDto.getCategoryId()));

        // Load video Service to get all videos of the course
//        courseDto.setVideos(getVideosOfCourse(course.getId()));

        return courseDto;
    }

    @Override
    public CourseDto update(CourseDto courseDto, String courseId) {
        Course course = courseRepo.findById(courseId).orElseThrow(() ->
                new ResourceNotFoundException("No course found, Pls tell me which one to update"));
        modelMapper.map(courseDto,course);
        Course savedCourse = courseRepo.save(course);
        return modelMapper.map(savedCourse,CourseDto.class);
    }

    @Override
    public ResponseEntity<?> delete(String courseId) {

        courseRepo.deleteById(courseId);
//        Course course = courseRepo.findById(courseId).orElseThrow(()
//                -> new ResourceNotFoundException("Course not found to delete!"));
//        courseRepo.delete(course);
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted Successfully.");
    }

    @Override
    public List<CourseDto> searchCourses(String keyword) {
        List<Course> courseList = courseRepo.
                findByTitleContainingIgnoreCaseOrShortDescContainingIgnoreCase(keyword, keyword);
        return courseList.stream().map(course -> {
            CourseDto courseDto = modelMapper.map(course, CourseDto.class);

            courseDto.setCategoryDto(getCategoryOfCourse(courseDto.getCategoryId()));
//            load videos of searched course[Video Service]
//            courseDto.setVideos(getVideosOfCourse(courseDto.getId()));
            return courseDto;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<CourseDto> getAllCoursesPages(Pageable pageable) {
        Page<Course>  courseList = courseRepo.findAll(pageable);

//        List<CourseDto> courseDtoList = courseList.stream().map(
//                course -> modelMapper.map(course, CourseDto.class)).toList();

        //  call one by one category api to get category details of the course

        // NO Need to fetch it again will call directly in above dtos only
//        List<CourseDto> newCourseDtoList = courseDtoList.stream().map( courseDto -> {
            // This is for getting categoryList from Course
//            ResponseEntity<List<CategoryDto>> exchange = restTemplate.exchange(
//                    AppConstants.DEFAULT_CATEGORY_URL + "/categories/" + courseDto.getCategoryId(),
//                    HttpMethod.GET,
//                    null,
//                    new ParameterizedTypeReference<List<CategoryDto>>() {
//                    });
//            List<CategoryDto> body = exchange.getBody();

            // For getting single category object
//            CategoryDto forObject = restTemplate.getForObject(
//                    AppConstants.DEFAULT_CATEGORY_URL + "/categories/" + courseDto.getCategoryId(),
//                    CategoryDto.class);
//            courseDto.setCategoryDto(forObject);
//            return courseDto;


            // upar wale me error aayegi agar kisi corse me category nhi hui toh
            // will do error handeling
            // do not repeat this we can make a method and call the method here
//            try{
//                ResponseEntity<CategoryDto> exchange = restTemplate.exchange(AppConstants.DEFAULT_CATEGORY_URL + "/categories/" + courseDto.getCategoryId(),
//                        HttpMethod.GET,null, CategoryDto.class);
//                courseDto.setCategoryDto(exchange.getBody());
//            }catch (HttpClientErrorException ex){
//                courseDto.setCategoryDto(null);
//                ex.printStackTrace();
//            }

//            courseDto.setCategoryDto(getCategoryOfCourse(courseDto.getCategoryId()));
//            return courseDto;
//        }).toList();
        List<CourseDto> courseDtoList = courseList.getContent().stream().map(
                course -> {
                    CourseDto courseDto = modelMapper.map(course, CourseDto.class);
                    // calling category-service to get category of course
                    courseDto.setCategoryDto(getCategoryOfCourse(courseDto.getCategoryId()));

                    // calling video-service to get all videos of the course
//                    courseDto.setVideos(getVideosOfCourse(course.getId()));
                    return courseDto;
                }).collect(Collectors.toList());

        return new PageImpl<>(courseDtoList, pageable, courseList.getTotalElements());
    }

    @Override
    public CourseDto saveBanner(MultipartFile file, String courseId) throws IOException {

        Course course = courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found!!"));
        String filePath = fileService.save(file, AppConstants.COURSE_BANNER_UPLOAD_DIR, file.getOriginalFilename());
        course.setBanner(filePath);
        course.setBannerContentType(file.getContentType());

        return modelMapper.map(courseRepo.save(course),CourseDto.class);
    }

    @Override
    public ResourceContentType getCourseBannerId(String courseId) {
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        String bannerPath = course.getBanner();
        Path path = Paths.get(bannerPath);
        Resource resource = new FileSystemResource(path);
        ResourceContentType resourceContentType = new ResourceContentType();
        resourceContentType.setResource(resource);
        resourceContentType.setContentType(course.getBannerContentType());
        return resourceContentType;
    }

    @Override
    public List<CourseDto> getCoursesOfCategory(String categoryId) {
        return this.courseRepo.findByCategoryId(categoryId)
                .stream().map(course -> modelMapper.map(course,CourseDto.class)).collect(Collectors.toList());
    }

    // get category of course(by loading category-service)
    // through [ REST-TEMPLATE]
    public CategoryDto getCategoryOfCourse(String categoryId){
        try{
            ResponseEntity<CategoryDto> exchange = restTemplate.exchange(
                    AppConstants.DEFAULT_CATEGORY_URL + "/categories/" + categoryId,
                    HttpMethod.GET,null, CategoryDto.class);

            HttpEntity<CategoryDto> categoryDtoHttpEntity = new HttpEntity<>(new CategoryDto());

           return exchange.getBody();

        }catch (HttpClientErrorException ex){
//            ex.printStackTrace();
            return null;
        }
    }

    // get all videos of course by loading video-service
    // through [WebClient]
    public List<VideoDto> getVideosOfCourse(String courseId){
        return webClient.build()
                .get()
                .uri(AppConstants.DEFAULT_VIDEO_BASE_URL + "/videos/course/{id}",courseId)
                .retrieve()
                .bodyToFlux(VideoDto.class)
                .collectList()
                .block();// means blocking application by default non-blocking manner me kam krta h
    }

    // way to add category to course [REST-TEMPLATE]
//    public CategoryDto addCategoryToCourse(String categoryId){
//        try{
//            HttpEntity<CategoryDto> categoryDtoHttpEntity = new HttpEntity<>(
//                    new CategoryDto());
//            restTemplate.exchange(AppConstants.DEFAULT_CATEGORY_URL + "/categories/" + categoryId,
//                    HttpMethod.POST,
//                    null,
//                    CategoryDto.class
//            );
//           return exchange.getBody();
//        }catch (HttpClientErrorException ex){
//            ex.printStackTrace();
//            return null;
//        }
//    }
}
