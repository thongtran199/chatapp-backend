package com.springboot.chatapp.service;

import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.dto.user.response.FoundUserResponseDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipService {
    Friendship sendFriendRequest(FriendshipRequestDTO friendshipRequestDTO);

    Friendship findById(Long friendshipId);

    List<Friendship> findPendingFriendshipsByRequesterId(Long userId);

    void revokeFriendRequest(Long friendshipId);

    void acceptFriendRequest(Long friendshipId);

    void declineFriendRequest(Long friendshipId);

    List<Friendship> findAllReceivedPendingFriendRequests(Long userId);

    void unFriend(Long userId1, Long userId2);

    Optional<Friendship> findLatestFriendshipBetweenUsers(Long user1, Long user2);

    List<Friendship> findAcceptedFriendshipsByUserId(Long userId);


}
