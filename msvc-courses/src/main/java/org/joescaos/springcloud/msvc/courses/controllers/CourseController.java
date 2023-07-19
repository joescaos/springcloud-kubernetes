package org.joescaos.springcloud.msvc.courses.controllers;

import org.joescaos.springcloud.msvc.courses.models.Course;
import org.joescaos.springcloud.msvc.courses.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        Optional<Course> courseById = courseService.getCourseById(id);
        if (courseById.isPresent()) {
            return ResponseEntity.ok(courseById.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        Course courseCreated = courseService.saveCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCourse(@RequestBody Course course,
                                        @PathVariable Long id) {
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
}
