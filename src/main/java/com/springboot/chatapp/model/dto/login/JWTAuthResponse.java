package com.springboot.chatapp.model.dto.login;

import com.springboot.chatapp.model.dto.user.UserProfileResponseDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JWTAuthResponse {
    private String accessToken;
    private UserProfileResponseDto user;
}
