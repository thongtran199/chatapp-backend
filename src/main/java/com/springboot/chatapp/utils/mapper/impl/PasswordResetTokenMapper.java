package com.springboot.chatapp.utils.mapper.impl;

import com.springboot.chatapp.model.dto.passwordReset.PasswordResetTokenResponseDto;
import com.springboot.chatapp.model.entity.PasswordResetToken;
import com.springboot.chatapp.utils.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetTokenMapper implements Mapper<PasswordResetToken, PasswordResetTokenResponseDto> {
    private ModelMapper modelMapper;
    public PasswordResetTokenMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;

    }
    @Override
    public PasswordResetTokenResponseDto mapToResponseDTO(PasswordResetToken passwordResetToken) {
        return modelMapper.map(passwordResetToken, PasswordResetTokenResponseDto.class);
    }

    @Override
    public PasswordResetToken mapToEntity(PasswordResetTokenResponseDto passwordResetTokenResponseDto) {
        return modelMapper.map(passwordResetTokenResponseDto, PasswordResetToken.class);
    }
}