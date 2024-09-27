package com.springboot.chatapp.domain.dto.user.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LatestMessageHistoryResponseDTO {
    private LocalDateTime createdAt;
    private String content;
}
