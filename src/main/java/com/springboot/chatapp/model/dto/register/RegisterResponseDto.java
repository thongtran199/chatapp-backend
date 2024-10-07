package com.springboot.chatapp.model.dto.register;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springboot.chatapp.model.dto.user.UserProfileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterResponseDto {
    private String accessToken;
    private UserProfileResponseDto user;
}
