package org.joescaos.springcloud.msvc.courses.clients;

import org.joescaos.springcloud.msvc.courses.models.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "msvc-users", url = "localhost:8001")
public interface UsersRestClient {

    @GetMapping("/api/users/{id}")
    UserDTO getUser(@PathVariable Long id);

    @PostMapping("/api/users")
    UserDTO createUser(@RequestBody UserDTO userDTO);
}
