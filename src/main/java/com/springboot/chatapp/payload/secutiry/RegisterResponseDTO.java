package com.springboot.chatapp.payload.secutiry;

import com.springboot.chatapp.domain.dto.user.response.UserProfileDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponseDTO {
    private String accessToken;
    private UserProfileDTO user;
}
