package com.springboot.chatapp.exception;

import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AlreadyHaveFriendshipIsPendingException extends RuntimeException{
    private FriendshipRequestDTO friendshipRequestDTO;

    public AlreadyHaveFriendshipIsPendingException(FriendshipRequestDTO friendshipRequestDTO) {
        super(String.format("Already have friendship PENDING between %s and %s", friendshipRequestDTO.getRequesterId(), friendshipRequestDTO.getRequestedUserId()));
        this.friendshipRequestDTO = friendshipRequestDTO;
    }

}
