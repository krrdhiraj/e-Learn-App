package com.service.video.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class VideoDto {
    private String id;

    private String title;

    private String desc;

    private String banner;

    private String length;

    private String fileSize;

    private String path;

    private String contentType;

    private Date addedDate;

    private String courseId;
}
