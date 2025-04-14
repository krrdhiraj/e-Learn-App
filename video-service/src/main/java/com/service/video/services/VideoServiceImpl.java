package com.service.video.services;

import com.service.video.documents.Video;
import com.service.video.dtos.VideoDto;
import com.service.video.exceptions.ResourceNotFoundException;
import com.service.video.repositories.VideoRepo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import java.util.Date;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService{

    private final ModelMapper modelMapper;
    private final VideoRepo videoRepo;

    public VideoServiceImpl(ModelMapper modelMapper, VideoRepo videoRepo) {
        this.modelMapper = modelMapper;
        this.videoRepo = videoRepo;
    }

    @Override
    public VideoDto create(VideoDto videoDto) {
        videoDto.setAddedDate(new Date());
        Video video = modelMapper.map(videoDto, Video.class);
        Video savedVideo = videoRepo.save(video);
        return modelMapper.map(savedVideo,VideoDto.class);
    }

    @Override
    public List<VideoDto> getAll() {
        List<Video> videoList = videoRepo.findAll();
        return videoList.stream().map(video -> modelMapper.map(video,VideoDto.class)).toList();
    }

    @Override
    public VideoDto getSingleVideo(String videoId) {
        Video video = videoRepo.findById(videoId).orElseThrow(() ->
                new ResourceNotFoundException("No video found with this id."));
        VideoDto videoDto = modelMapper.map(video, VideoDto.class);
        // calling course-service api to get course of the video
//        CourseDto courseById = courseService.getCourseById(videoDto.getCourseId());
        return modelMapper.map(video, VideoDto.class);
    }

    @Override
    public void deleteVideo(String videoId) {
        videoRepo.deleteById(videoId);
    }
    @Override
    public VideoDto updateVideo(VideoDto videoDto, String videoId) {
        Video video = videoRepo.findById(videoId).orElseThrow(() ->
                new ResourceNotFoundException("No video found to update with this id :- ." + videoId));
        modelMapper.map(videoDto,video);
        video.setAddedDate(new Date());
        video.setId(videoId);
        Video savedVideo = videoRepo.save(video);
        return modelMapper.map(savedVideo, VideoDto.class);
    }

    @Override
    public Page<VideoDto> getVideoPages(Pageable pageable) {

        Page<Video> videoPage = videoRepo.findAll(pageable);
        List<Video> videoList = videoPage.getContent();
        List<VideoDto> videoDtoList = videoList.stream().map(video ->
                modelMapper.map(video, VideoDto.class)).toList();
        
        return new PageImpl<>(videoDtoList, pageable, videoPage.getTotalElements());
    }

    @Override
    public List<VideoDto> searchVideos(String keyword) {
        List<Video> videoList = videoRepo.findByTitleContainingIgnoreCaseOrDescContainingIgnoreCase(keyword,keyword);

        return videoList.stream().map(video ->
                modelMapper.map(video,VideoDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<VideoDto> findAllVideoOfCourse(String courseId) {
        return videoRepo.findByCourseId(courseId).stream().map(video ->
                modelMapper.map(video, VideoDto.class)).toList();
    }

    @Override
    public VideoDto saveVideoFile(MultipartFile file, String videoId) {
        return null;
    }

    @Override
    public List<VideoDto> getVideoOfCourse(String courseId) {
        return videoRepo.findByCourseId(courseId).stream().map(
                video-> modelMapper.map(video,VideoDto.class)).collect(Collectors.toList());
    }

}
