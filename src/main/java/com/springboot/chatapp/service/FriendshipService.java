package com.springboot.chatapp.service;

import com.springboot.chatapp.model.dto.friendship.FriendshipRequestDto;
import com.springboot.chatapp.model.entity.Friendship;

import java.util.List;
import java.util.Optional;

public interface FriendshipService {
    Friendship sendFriendRequest(FriendshipRequestDto friendshipRequestDTO);

    Friendship findById(Long friendshipId);

    List<Friendship> findPendingFriendshipsByRequesterId(Long userId);

    void revokeFriendRequest(Long friendshipId);

    void acceptFriendRequest(Long friendshipId);

    void declineFriendRequest(Long friendshipId);

    List<Friendship> findAllReceivedPendingFriendRequests(Long userId);

    void unFriend(Long userId1, Long userId2);

    Optional<Friendship> findLatestFriendshipBetweenUsers(Long user1, Long user2);

    List<Friendship> findAcceptedFriendshipsByUserId(Long userId);

    void throwExceptionIfFriendshipIsNotPending(Friendship friendship);



}
