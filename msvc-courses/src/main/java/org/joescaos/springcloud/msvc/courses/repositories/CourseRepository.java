package org.joescaos.springcloud.msvc.courses.repositories;

import org.joescaos.springcloud.msvc.courses.models.Course;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {

    @Modifying
    @Query(value = "delete from courses_users cu where cu.user_id = ?1 ",
    nativeQuery = true)
    void deleteCourseUserById(Long id);

}
