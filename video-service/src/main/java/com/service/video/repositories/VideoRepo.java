package com.service.video.repositories;

import com.service.video.documents.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepo extends MongoRepository<Video, String> {

    Optional<Video> findByTitle(String title);

//    List<Video> findByCourse(Course course);

    List<Video> findByTitleContainingIgnoreCaseOrDescContainingIgnoreCase(String keyword, String keyword1);
//    List<Video> findByTitleContainingIgnoreCaseOrDescContainingIgnoreCase(String keyword);

    List<Video> findByCourseId(String courseId);
}
