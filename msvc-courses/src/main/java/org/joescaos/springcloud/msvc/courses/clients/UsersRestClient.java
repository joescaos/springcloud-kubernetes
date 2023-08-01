package org.joescaos.springcloud.msvc.courses.clients;

import org.joescaos.springcloud.msvc.courses.models.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-users", url = "msvc-users:8001")
public interface UsersRestClient {

    @GetMapping("/api/users/{id}")
    UserDTO getUser(@PathVariable Long id);

    @PostMapping("/api/users")
    UserDTO createUser(@RequestBody UserDTO userDTO);

    @GetMapping("api/users/users-by-course")
    List<UserDTO> getUsersByCourse(@RequestParam("ids") List<Long> ids);
}
