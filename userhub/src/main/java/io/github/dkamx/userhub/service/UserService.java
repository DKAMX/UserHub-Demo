package io.github.dkamx.userhub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.dkamx.userhub.dto.UserPostRequest;
import io.github.dkamx.userhub.entity.User;
import io.github.dkamx.userhub.repository.UserRepository;
import io.github.dkamx.userhub.util.Md5Util;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public static String USER_NOT_FOUND_MESSAGE = "用户未找到";
    public static String USER_UPDATE_SUCCESS_MESSAGE = "用户信息更新成功";
    public static String USER_CREATE_SUCCESS_MESSAGE = "用户创建成功";
    public static String USER_DELETE_SUCCESS_MESSAGE = "用户删除成功";
    public static String USER_EXISTS_MESSAGE = "用户名已存在";

    public Map<String, Object> createUser(User user) {
        user.setPassword(Md5Util.encrypt(user.getPassword()));
        user = userRepository.save(user);
        return Map.of("message", USER_CREATE_SUCCESS_MESSAGE,
                "user_id", user.getId());
    }

    public List<Map<String, Object>> getAllUsers() {
        var users = userRepository.findAll();
        var list = new ArrayList<Map<String, Object>>();
        users.forEach(user -> list.add(user.toMap()));
        return list;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Map<String, Object> deleteUser(Long id) {
        if (!userRepository.findById(id).isPresent()) {
            return Map.of("message", USER_NOT_FOUND_MESSAGE);
        }
        userRepository.deleteById(id);
        return Map.of("message", USER_DELETE_SUCCESS_MESSAGE);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Map<String, Object> updateUser(Long id, UserPostRequest request) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (request.getName() != null) {
                user.setName(request.getName());
            }
            if (request.getBirthdate() != null) {
                user.setBirthdate(request.getBirthdate());
            }
            if (request.getGender() != null) {
                if (request.getGender().equals("M")) {
                    user.setGender(true);
                } else if (request.getGender().equals("F")) {
                    user.setGender(false);
                } else {
                    throw new IllegalArgumentException("Invalid gender: " + request.getGender());
                }
            }
            userRepository.save(user);
            return Map.of("message", USER_UPDATE_SUCCESS_MESSAGE);
        } else {
            return Map.of("message", USER_NOT_FOUND_MESSAGE);
        }
    }
}
