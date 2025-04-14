package com.service.video.controllers;
import com.service.video.dtos.CustomMessage;
import com.service.video.dtos.VideoDto;
import com.service.video.dtos.VideoUploadResponse;
import com.service.video.services.VideoService;
import com.service.video.services.VideoUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    private final VideoService videoService;
    private final VideoUploadService uploadService;
    private Logger logger = LoggerFactory.getLogger(VideoController.class);
    public VideoController(VideoService videoService, VideoUploadService uploadService){
        this.videoService = videoService;
        this.uploadService = uploadService;
    }

    // create new video
    @PostMapping
    public ResponseEntity<VideoDto> create(
            @RequestBody VideoDto videoDto
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(videoService.create(videoDto));
    }

    // get all videos
    @GetMapping
    public ResponseEntity<List<VideoDto>> getAll(){
        return ResponseEntity.ok(videoService.getAll());
    }

    // get Single video
    @GetMapping("/{videoId}")
    public ResponseEntity<VideoDto >getSingleVideo(
            @PathVariable String videoId
    ){
        return ResponseEntity.ok(videoService.getSingleVideo(videoId));
    }

    // get all videos with pages
    @GetMapping("/pages")
    public ResponseEntity<Page<VideoDto>> getVideos(Pageable pageable){
        return ResponseEntity.ok(videoService.getVideoPages(pageable));
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<CustomMessage> deleteVideo(
            @PathVariable String videoId
    ){
        videoService.deleteVideo(videoId);
        CustomMessage customMessage = new CustomMessage("video deleted successfully.",true, HttpStatus.OK);
        return ResponseEntity.ok(customMessage);
    }

    // update video
    @PutMapping("/{videoId}")
    public ResponseEntity<VideoDto> updateVideo(
            @RequestBody VideoDto videoDto,
            @PathVariable String videoId
    ){
        return ResponseEntity.ok(videoService.updateVideo(videoDto, videoId));
    }


//    @CircuitBreaker(name = "", fallbackMethod = "")
    @GetMapping("/search")
    public ResponseEntity<List<VideoDto>> searchVideos(@RequestParam String keyword){
        return ResponseEntity.ok(videoService.searchVideos(keyword));
    }
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> findAllVideoOfCourse(@PathVariable String courseId){
        List<VideoDto> allVideoOfCourse = videoService.findAllVideoOfCourse(courseId);
        return ResponseEntity.ok(allVideoOfCourse);
    }

    @PostMapping("/upload")
    public VideoUploadResponse uploadVideo(
            @RequestParam(value = "courseId", required = true) String courseId, // required = true -> means mandatory (but by default true hi hota h)
            @RequestParam("videoId") String videoId,
            @RequestParam("videoFile")MultipartFile videoFile
    ){
        VideoUploadResponse uploadResponse = uploadService.uploadVideo(courseId, videoId, videoFile);
        return uploadResponse;
    }

    //serving video
    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> streamVideo(
            @PathVariable String videoId,
            @RequestHeader(value = "Range", required = false) String rangeHeader) { // to stream video partially

        try {
            logger.info(rangeHeader);

            // Get the video file details
            VideoDto videoDto = videoService.getSingleVideo(videoId);
            String filePathString = videoDto.getPath();

            Path filePath = Paths.get(filePathString);
            Resource videoResource = new UrlResource(filePath.toUri());

            if (!videoResource.exists() || !videoResource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            long fileSize = videoResource.contentLength();
            long start = 0, end = fileSize - 1;

            // Parse Range Header if present
            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                String[] range = rangeHeader.replace("bytes=", "").split("-");


                try {
                    start = Long.parseLong(range[0]);

                    if (range.length > 1 && !range[1].isEmpty()) {
                        end = Long.parseLong(range[1]);
                    }
                    logger.info("{ } - { }", start, end);
                } catch (NumberFormatException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            }

            // Validate the range
            if (start > end || end >= fileSize) {

                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                        .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize)
                        .build();
            }

            // Create Partial Content Response
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, videoDto.getContentType());
            //bytes 0-999/10000
            headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize);
            headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");

            InputStream inputStream = Files.newInputStream(filePath);
            //this is very important:Moves the pointer to the start byte for the requested range.
            inputStream.skip(start); // Skip to the start of the range

            InputStreamResource resource = new InputStreamResource(inputStream);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .contentLength(end - start + 1)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
