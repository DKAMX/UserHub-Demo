package io.github.dkamx.userhub.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.dkamx.userhub.dto.UserPostRequest;
import io.github.dkamx.userhub.entity.User;
import io.github.dkamx.userhub.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<Map<String, Object>> listUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return ResponseEntity.ok(user.toMap());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserPostRequest userRequest) {
        var username = userRequest.getUsername();
        if (userService.getUserByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", UserService.USER_EXISTS_MESSAGE));
        }

        return ResponseEntity.ok(userService.createUser(User.of(userRequest)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody UserPostRequest request) {
        var result = userService.updateUser(id, request);
        if (result.get("message").equals(UserService.USER_UPDATE_SUCCESS_MESSAGE)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        var result = userService.deleteUser(id);
        if (result.get("message").equals(UserService.USER_DELETE_SUCCESS_MESSAGE)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
