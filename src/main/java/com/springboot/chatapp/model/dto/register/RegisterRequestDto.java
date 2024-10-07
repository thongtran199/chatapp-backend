package com.springboot.chatapp.model.dto.register;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotNull(message = "fullName cannot be null")
    @NotEmpty(message = "fullName cannot be empty")
    private String fullName;

    @NotNull(message = "username cannot be null")
    @NotEmpty(message = "username cannot be empty")
    private String username;

    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]+$",
            message = "Password must contain at least one uppercase letter, one number, and one special symbol.")
    private String password;

}
