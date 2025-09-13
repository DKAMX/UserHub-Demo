package io.github.dkamx.userhub.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.dkamx.userhub.dto.LoginPostRequest;
import io.github.dkamx.userhub.entity.User;
import io.github.dkamx.userhub.repository.UserRepository;
import io.github.dkamx.userhub.service.LoginService;
import io.github.dkamx.userhub.util.JwtUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final LoginService loginService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginPostRequest loginRequest) {
        Map<String, Object> result = loginService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (result.get("message").equals("登录成功")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
    }

    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> auth(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("message", "无效的Authorization头"));
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            Long userId = jwtUtil.extractUserId(token);

            if (!jwtUtil.validateToken(token, username)) {
                return ResponseEntity.badRequest().body(Map.of("message", "无效的JWT令牌"));
            }

            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                return ResponseEntity.ok(Map.of(
                        "message", "验证成功",
                        "user_id", user.getId(),
                        "username", user.getUsername()));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "用户不存在"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "JWT令牌解析失败"));
        }
    }
}