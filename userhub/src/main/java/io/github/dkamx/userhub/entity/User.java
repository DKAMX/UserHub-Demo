package io.github.dkamx.userhub.entity;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import io.github.dkamx.userhub.dto.UserPostRequest;
import lombok.Data;

@Data
@Table("users")
public class User {
    @Id
    private Long id;

    @Column("fullname")
    private String name;

    @Column("username")
    private String username;

    @Column("password_hash")
    private String password;

    @Column("birthdate")
    private LocalDate birthdate;

    @Column("gender")
    private Boolean gender; // true for male, false for female

    public Map<String, Object> toMap() {
        return Map.of(
                "id", id,
                "name", name,
                "username", username,
                "birthdate", birthdate,
                "gender", gender ? "M" : "F");
    }

    public static User of(UserPostRequest request) {
        var user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setBirthdate(request.getBirthdate());

        var gender = request.getGender();
        if (gender.equals("M")) {
            user.setGender(true);
        } else if (gender.equals("F")) {
            user.setGender(false);
        } else {
            throw new IllegalArgumentException("Invalid gender: " + gender);
        }
        return user;
    }
}
