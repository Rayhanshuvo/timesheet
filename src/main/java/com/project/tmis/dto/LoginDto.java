package com.project.tmis.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {
    @NotBlank(message = "Username mandatory")
    private String username;
    @NotBlank(message = "Password mandatory")
    private String password;
}