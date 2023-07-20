package org.joescaos.springcloud.msvc.courses.services;

import org.joescaos.springcloud.msvc.courses.models.Course;
import org.joescaos.springcloud.msvc.courses.models.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> getCourses();

    Optional<Course> getCourseById(Long id);

    Course saveCourse(Course course);

    void deleteCourse(Course course);

    Optional<UserDTO> assignUser(UserDTO userDTO, Long courseId);

    Optional<UserDTO> createUser(UserDTO userDTO, Long courseId);

    Optional<UserDTO> deleteUserFromCourse(UserDTO userDTO, Long courseId);

    Optional<Course> getCourseWithUsers(Long id);
}
