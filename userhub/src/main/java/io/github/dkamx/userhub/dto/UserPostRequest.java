package io.github.dkamx.userhub.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserPostRequest {
    private String name;
    private String username;
    private String password;
    private LocalDate birthdate;
    private String gender;
}
