package com.springboot.chatapp.service;

import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipService {
    Friendship save(FriendshipRequestDTO friendshipRequestDTO);

    Friendship findById(Long friendshipId);

    List<Friendship> findAllAcceptedFriends(Long userId);

    List<Friendship> findAllSentFriendRequests(Long userId);

    List<Friendship> findAllDeclinedFriendRequests(Long userId);

    void cancelFriendRequest(Long requesterId, Long requestedUserId);

    void acceptFriendRequest(Long requesterId, Long requestedUserId);

    void declineFriendRequest(Long requesterId, Long requestedUserId);

    Friendship findAcceptedFriendshipBetweenUsers(Long requesterId, Long requestedUserId);

    boolean existsById(Long friendshipId);

    List<Friendship> findAllFriendsByUserId(Long userId);
}
