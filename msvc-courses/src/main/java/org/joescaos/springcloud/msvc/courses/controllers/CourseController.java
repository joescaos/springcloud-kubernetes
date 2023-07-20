package org.joescaos.springcloud.msvc.courses.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import org.joescaos.springcloud.msvc.courses.models.Course;
import org.joescaos.springcloud.msvc.courses.models.dto.UserDTO;
import org.joescaos.springcloud.msvc.courses.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        Optional<Course> courseById = courseService.getCourseWithUsers(id);
        if (courseById.isPresent()) {
            return ResponseEntity.ok(courseById.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody Course course,
                                          BindingResult result) {
        if (result.hasErrors()) {
            return responseWithErrors(result);
        }
        Course courseCreated = courseService.saveCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCourse(@RequestBody Course course,
                                        BindingResult result,
                                        @PathVariable Long id) {
        if (result.hasErrors()) {
            return responseWithErrors(result);
        }
        Optional<Course> courseToEdit = courseService.getCourseById(id);
        if (courseToEdit.isPresent()) {
            Course courseEdited = courseToEdit.get();
            courseEdited.setName(course.getName());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(courseService.saveCourse(courseEdited));

        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        Optional<Course> courseToDelete = courseService.getCourseById(id);
        if (courseToDelete.isPresent()) {
            courseService.deleteCourse(courseToDelete.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/assign-user/{courseId}")
    public ResponseEntity<?> assignUser(@RequestBody UserDTO user, @PathVariable Long courseId) {
        Optional<UserDTO> userDTO;
        try {
            userDTO = courseService.assignUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Collections.singletonMap("message", e.getMessage()));
        }
        if (userDTO.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create-user/{courseId}")
    public ResponseEntity<?> createUser(@RequestBody UserDTO user, @PathVariable Long courseId) {
        Optional<UserDTO> userDTO;
        try {
            userDTO = courseService.createUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Collections.singletonMap("message", e.getMessage()));
        }
        if (userDTO.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-user/{courseId}")
    public ResponseEntity<?> deleteUser(@RequestBody UserDTO user, @PathVariable Long courseId) {
        Optional<UserDTO> userDTO;
        try {
            userDTO = courseService.deleteUserFromCourse(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Collections.singletonMap("message", e.getMessage()));
        }
        if (userDTO.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Map<String, String>> responseWithErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(
                err -> errors.put(err.getField(),
                        String.format("Field %s %s",
                                err.getField(),
                                err.getDefaultMessage()))
        );

        return ResponseEntity.badRequest().body(errors);
    }
}
