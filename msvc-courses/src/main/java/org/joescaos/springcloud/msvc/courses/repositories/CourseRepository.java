package org.joescaos.springcloud.msvc.courses.repositories;

import org.joescaos.springcloud.msvc.courses.models.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {

}
