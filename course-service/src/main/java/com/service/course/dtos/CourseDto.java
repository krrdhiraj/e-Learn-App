package com.service.course.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import java.util.Date;
import java.util.List;

@Data
public class CourseDto {

    private String id;

    private String title;

    private String shortDesc;

    @JsonProperty("long_description") // give our own custom name
    private String longDesc;

    private double price;

    private boolean live = false;

    private double discount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/mm/dd HH:mm:ss", timezone = "IST")
    private Date createdDate;

    private String banner;

    private String bannerContentType;

    public String getBannerUrl() {
        return "http://localhost:9092/api/v1/courses/" + id + "/banners";
    }

    private String categoryId;

    private CategoryDto categoryDto;

    private List<VideoDto> videos;
}