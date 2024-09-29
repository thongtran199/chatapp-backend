package com.springboot.chatapp.exception;

import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FriendshipStatusNotIsPendingException extends RuntimeException{
    private Long friendshipId;
    private FriendshipStatus friendshipStatus;

    public FriendshipStatusNotIsPendingException(Long friendshipId, FriendshipStatus friendshipStatus) {
        super(String.format("FriendshipStatus in friendshipId %s is %s, not is PENDING", friendshipId, friendshipStatus));
        this.friendshipId = friendshipId;
        this.friendshipStatus = friendshipStatus;
    }

}
