package org.joescaos.springcloud.msvc.users.msvcusers.repositories;

import org.joescaos.springcloud.msvc.users.msvcusers.models.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
