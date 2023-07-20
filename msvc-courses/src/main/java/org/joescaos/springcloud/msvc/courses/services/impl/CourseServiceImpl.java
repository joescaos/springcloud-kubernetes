package org.joescaos.springcloud.msvc.courses.services.impl;

import org.joescaos.springcloud.msvc.courses.clients.UsersRestClient;
import org.joescaos.springcloud.msvc.courses.models.Course;
import org.joescaos.springcloud.msvc.courses.models.CourseUser;
import org.joescaos.springcloud.msvc.courses.models.dto.UserDTO;
import org.joescaos.springcloud.msvc.courses.repositories.CourseRepository;
import org.joescaos.springcloud.msvc.courses.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UsersRestClient usersRestClient;

    @Override
    @Transactional(readOnly = true)
    public List<Course> getCourses() {
        return (List<Course>) courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    @Transactional
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Course course) {
        courseRepository.delete(course);
    }

    @Override
    @Transactional
    public void deleteCourseUserById(Long id) {
        courseRepository.deleteCourseUserById(id);
    }

    @Override
    @Transactional
    public Optional<UserDTO> assignUser(UserDTO userDTO, Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            UserDTO user = usersRestClient.getUser(userDTO.getId());
            Course course = courseOptional.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(user.getId());

            course.addCourseUser(courseUser);
            courseRepository.save(course);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<UserDTO> createUser(UserDTO userDTO, Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            UserDTO user = usersRestClient.createUser(userDTO);
            Course course = courseOptional.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(user.getId());

            course.addCourseUser(courseUser);
            courseRepository.save(course);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<UserDTO> deleteUserFromCourse(UserDTO userDTO, Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            UserDTO user = usersRestClient.getUser(userDTO.getId());
            Course course = courseOptional.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(user.getId());

            course.removeCourseUser(courseUser);
            courseRepository.save(course);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Course> getCourseWithUsers(Long id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            if (!course.getCourseUsers().isEmpty()) {
                List<Long> usersIds = course.getCourseUsers().stream()
                        .map(CourseUser::getUserId).toList();
                List<UserDTO> usersInCourse = usersRestClient.getUsersByCourse(usersIds);
                course.setUsers(usersInCourse);
            }
            return Optional.of(course);
        }
        return Optional.empty();
    }
}
