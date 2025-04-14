package com.service.video;

import com.service.video.repositories.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VideoServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(VideoServiceApplication.class, args);
	}

	@Autowired
	private VideoRepo videoRepo;
	@Override
	public void run(String... args) throws Exception {
//		Video video = new Video();
//		video.setDesc("Learn Spring and Springboot in one video from scratch : Beginner to Pro");
//		video.setTitle("Learn Spring&Springboot");
//		video.setContentType("video/mp4");
//		video.setFileSize("6.49GB");
//		video.setFilePath("/videos/spring_springboot.mp4");
//
//		Video save = videoRepo.save(video);
//		videoRepo.findAll().forEach(System.out::println);

//		System.out.println(save);
	}
}
