package com.springboot.chatapp.model.dto.register;

import com.springboot.chatapp.model.dto.user.UserProfileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RegisterResponseDto {
    private String accessToken;
    private UserProfileResponseDto user;
}
