package com.springboot.chatapp.model.dto.login;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {

    @NotNull(message = "usernameOrEmail cannot be null")
    @NotEmpty(message = "usernameOrEmail cannot be empty")
    private String usernameOrEmail;

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message = "password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]+$",
            message = "password must contain at least one uppercase letter, one number, and one special symbol.")
    private String password;
}
