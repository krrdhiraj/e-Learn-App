package com.service.video.services;

import com.service.video.dtos.VideoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    VideoDto create(VideoDto videoDto);

    List<VideoDto> getAll();

    VideoDto getSingleVideo(String videoId);

    void deleteVideo(String videoId);

    VideoDto updateVideo(VideoDto videoDto, String videoId);

    Page<VideoDto> getVideoPages(Pageable pageable);

    List<VideoDto> searchVideos(String keyword);

    List<VideoDto> findAllVideoOfCourse(String courseId);

    VideoDto saveVideoFile(MultipartFile file, String videoId);

    List<VideoDto> getVideoOfCourse(String courseId);
}
