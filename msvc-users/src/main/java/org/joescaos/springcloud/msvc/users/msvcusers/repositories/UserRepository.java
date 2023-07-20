package org.joescaos.springcloud.msvc.users.msvcusers.repositories;

import org.joescaos.springcloud.msvc.users.msvcusers.models.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
