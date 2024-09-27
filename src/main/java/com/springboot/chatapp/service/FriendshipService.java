package com.springboot.chatapp.service;

import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipService {
    Friendship save(FriendshipRequestDTO friendshipRequestDTO);

    Friendship findById(Long friendshipId);

    List<Friendship> findAllSentFriendRequests(Long userId);

    void cancelFriendRequest(Long requesterId, Long requestedUserId);

    void acceptFriendRequest(Long requesterId, Long requestedUserId);

    void declineFriendRequest(Long requesterId, Long requestedUserId);

    List<Friendship> findAllFriendsByUserId(Long userId);

    List<Friendship> findAllReceivedPendingFriendRequests(Long userId);
    Optional<Friendship> getFriendshipBetweenUsers(Long userId1, Long userId2);

    Friendship findFriendshipByRequesterIdAndRequestedUserId(Long requesterId, Long requestedUserId);

}
