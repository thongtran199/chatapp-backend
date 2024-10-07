package com.springboot.chatapp.model.dto.friendship;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipRequestDto {
    @NotNull(message = "Requester ID cannot be null")
    @Min(value = 1, message = "Requester ID must be a positive number")
    @Schema(description = "ID of the user making the request", required = true)
    private Long requesterId;

    @NotNull(message = "Requested user ID cannot be null")
    @Min(value = 1, message = "Requested user ID must be a positive number")
    @Schema(description = "ID of the user being requested", required = true)
    private Long requestedUserId;
}
