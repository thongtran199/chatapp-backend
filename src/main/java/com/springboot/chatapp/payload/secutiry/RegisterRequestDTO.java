package com.springboot.chatapp.payload.secutiry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String rePassword;
}
