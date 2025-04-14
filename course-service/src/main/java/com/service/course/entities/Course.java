package com.service.course.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "courses")
@Data
public class Course {

    @Id
    private String id;

    private String title;

    private String shortDesc;

    @Column(length = 2000)
    private String longDesc;

    private double price;

    private boolean live=false;

    private double discount;

    private Date createdDate;

    private String banner;

    private String bannerContentType;

    private String categoryId;
}
