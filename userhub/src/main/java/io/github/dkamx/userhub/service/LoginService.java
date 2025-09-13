package io.github.dkamx.userhub.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.dkamx.userhub.entity.User;
import io.github.dkamx.userhub.repository.UserRepository;
import io.github.dkamx.userhub.util.JwtUtil;
import io.github.dkamx.userhub.util.Md5Util;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public Map<String, Object> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String hashedPassword = Md5Util.encrypt(password);
            if (user.getPassword().equals(hashedPassword)) {
                String token = jwtUtil.generateToken(username, user.getId());
                return Map.of(
                        "message", "登录成功",
                        "user_id", user.getId(),
                        "token", token);
            } else {
                return Map.of("message", "密码错误");
            }
        } else {
            return Map.of("message", "账号不存在");
        }
    }
}