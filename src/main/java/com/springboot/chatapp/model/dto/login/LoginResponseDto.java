package com.springboot.chatapp.model.dto.login;

import com.springboot.chatapp.model.dto.user.UserProfileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private UserProfileResponseDto user;
}
