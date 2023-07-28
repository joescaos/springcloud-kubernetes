package org.joescaos.springcloud.msvc.users.msvcusers.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-courses", url = "msvc-courses:8002")
public interface CourseRestClient {

    @DeleteMapping("/api/courses/delete-user-from-course/{id}")
    void deleteUser(@PathVariable Long id);
}
