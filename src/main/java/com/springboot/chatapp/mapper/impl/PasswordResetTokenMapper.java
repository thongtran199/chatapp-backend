package com.springboot.chatapp.mapper.impl;

import com.springboot.chatapp.domain.dto.user.response.PasswordResetTokenResponseDTO;
import com.springboot.chatapp.domain.entity.PasswordResetToken;
import com.springboot.chatapp.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetTokenMapper implements Mapper<PasswordResetToken, PasswordResetTokenResponseDTO> {
    private ModelMapper modelMapper;
    public PasswordResetTokenMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;

    }
    @Override
    public PasswordResetTokenResponseDTO mapToResponseDTO(PasswordResetToken passwordResetToken) {
        return modelMapper.map(passwordResetToken, PasswordResetTokenResponseDTO.class);
    }

    @Override
    public PasswordResetToken mapToEntity(PasswordResetTokenResponseDTO passwordResetTokenResponseDto) {
        return modelMapper.map(passwordResetTokenResponseDto, PasswordResetToken.class);
    }
}