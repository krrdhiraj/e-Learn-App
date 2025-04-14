package com.service.course.repositories;

import com.service.course.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepo extends JpaRepository<Course, String> {

    Optional<Course> findByTitle(String title);

    List<Course> findByLive(boolean live);

    List<Course> findByTitleContainingIgnoreCaseOrShortDescContainingIgnoreCase(String keyword, String keyword1);

//    @Query("select c from Course c where c.category.i")
    List<Course> findByCategoryId(String categoryId);
}
