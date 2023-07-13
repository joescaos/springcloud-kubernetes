package org.joescaos.springcloud.msvc.users.msvcusers.services;

import org.joescaos.springcloud.msvc.users.msvcusers.models.entities.User;

import java.util.List;
import java.util.Optional;

public interface UsersService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User save(User user);

    void delete(Long id);
}
