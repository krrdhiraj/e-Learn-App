package com.service.video.services;

import com.service.video.documents.Video;
import com.service.video.dtos.VideoDto;
import com.service.video.dtos.VideoUploadResponse;
import com.service.video.repositories.VideoRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class VideoUploadServiceImpl implements VideoUploadService {

    private VideoRepo videoRepo;

    private ModelMapper modelMapper;



    @Value("${video.upload.path}")
    private String uploadPath;

    public VideoUploadServiceImpl(VideoRepo videoRepo, ModelMapper modelMapper) {
        this.videoRepo = videoRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public VideoUploadResponse uploadVideo(String courseId, String videoId, MultipartFile videoFile) {

        //video service upload code.
        VideoUploadResponse videoUploadResponse = new VideoUploadResponse();


        if (videoFile.isEmpty()) {
            videoUploadResponse.setMessage("File is empty !!");
            videoUploadResponse.setSuccess(false);
            return videoUploadResponse;
        }

        //validate content type
        String contentType = videoFile.getContentType();

        if (contentType == null || !contentType.startsWith("video/")) {
            videoUploadResponse.setMessage("Invalid File !!");
            videoUploadResponse.setSuccess(false);
            return videoUploadResponse;
        }

        //create path for upload Directory

        //upload code
        try {
            Path path = Paths.get(uploadPath);

            //if folder does not exists then create
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            // file name of upload video
            String filename = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();


            Path mainPath = path.resolve(filename);

            // get the video
            Video video = videoRepo.findById(videoId).orElseThrow(() -> new RuntimeException("Video metadata not found !!"));

            String oldVideoFilePath = video.getPath();
            Path oldFillePath = Paths.get(oldVideoFilePath);
            if (Files.exists(oldFillePath)) {
                Files.delete(oldFillePath);
            }
            System.out.println(mainPath);
//            videoFile.transferTo(mainPath); // copy files to main path
            Files.copy(videoFile.getInputStream(), mainPath, StandardCopyOption.REPLACE_EXISTING); // copy files to main path

            video.setPath(mainPath.toString());
            video.setContentType(contentType);
            video.setCourseId(courseId);
            videoRepo.save(video);
            videoUploadResponse.setMessage("Video File uploaded Successfully");
            ///ENCODE VIDEO METHOD: VIDEO.
            videoUploadResponse.setSuccess(true);
            videoUploadResponse.setVideoDto(modelMapper.map(video, VideoDto.class));
            return videoUploadResponse;

        } catch (IOException e) {
            videoUploadResponse.setMessage(e.getMessage());
            videoUploadResponse.setSuccess(false);
            e.printStackTrace();
            return videoUploadResponse;
        }

    }
}