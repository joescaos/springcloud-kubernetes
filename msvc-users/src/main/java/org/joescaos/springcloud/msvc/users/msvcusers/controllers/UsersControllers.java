package org.joescaos.springcloud.msvc.users.msvcusers.controllers;

import jakarta.validation.Valid;
import org.joescaos.springcloud.msvc.users.msvcusers.models.entities.User;
import org.joescaos.springcloud.msvc.users.msvcusers.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UsersControllers {

    @Autowired
    private UsersService usersService;


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(usersService.getAllUsers(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = usersService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return responseWithErrors(result);
        }
        if (!user.getEmail().isEmpty() && usersService.userExistsByEmail(user.getEmail()))
            return ResponseEntity.badRequest().body(Collections.singletonMap("Email", "email already exists"));
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user,
                                        BindingResult result,
                                        @PathVariable Long id) {
        if (result.hasErrors()) {
            return responseWithErrors(result);
        }

        Optional<User> userById = usersService.getUserById(id);
        if (userById.isPresent()) {
            User currentUser = userById.get();
            if (!user.getEmail().isEmpty()
                    && !user.getEmail().equalsIgnoreCase(currentUser.getEmail())
                    && usersService.userExistsByEmail(user.getEmail()))
                return ResponseEntity.badRequest().body(Collections.singletonMap("Email", "email already exists"));
            currentUser.setName(user.getName());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());
            return new ResponseEntity<>(usersService.save(currentUser), HttpStatus.CREATED);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        Optional<User> user = usersService.getUserById(id);
        if (user.isPresent()) {
            usersService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users-by-course")
    public ResponseEntity<?> getUsersByCourse(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(usersService.getUsersByIds(ids));
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
