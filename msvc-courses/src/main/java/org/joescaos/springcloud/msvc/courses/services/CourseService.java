package org.joescaos.springcloud.msvc.courses.services;

import org.joescaos.springcloud.msvc.courses.models.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> getCourses();

    Optional<Course> getCourseById(Long id);

    Course saveCourse(Course course);

    void deleteCourse(Course course);
}
