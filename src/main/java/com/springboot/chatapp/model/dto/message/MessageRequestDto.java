package com.springboot.chatapp.model.dto.message;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDto {
    @NotNull(message = "Message sender ID cannot be null")
    @Min(value = 1, message = "Message sender ID must be a positive number")
    private Long messageSenderId;

    @NotNull(message = "Message receiver ID cannot be null")
    @Min(value = 1, message = "Message receiver ID must be a positive number")
    private Long messageReceiverId;

    @NotNull(message = "Content cannot be null")
    @NotEmpty(message = "Content cannot be empty")
    @Size(min = 1, max = 500, message = "Content must be between 1 and 500 characters")
    private String content;
}
