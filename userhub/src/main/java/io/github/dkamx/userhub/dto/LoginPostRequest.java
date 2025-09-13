package io.github.dkamx.userhub.dto;

import lombok.Data;

@Data
public class LoginPostRequest {
    private String username;
    private String password;
}