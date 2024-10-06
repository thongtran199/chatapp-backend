package com.springboot.chatapp.service.impl;

import com.springboot.chatapp.model.dto.friendship.FriendshipRequestDto;
import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.enums.FriendshipStatus;
import com.springboot.chatapp.model.exception.ChatAppAPIException;
import com.springboot.chatapp.model.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.FriendshipRepository;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserService userService;

    public FriendshipServiceImpl(
            FriendshipRepository friendshipRepository,
            UserService userService) {

        this.friendshipRepository = friendshipRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Friendship sendFriendRequest(FriendshipRequestDto friendshipRequestDTO) {
        Friendship friendship = new Friendship();
        User requester = userService.findById(friendshipRequestDTO.getRequesterId());
        User requestedUser = userService.findById(friendshipRequestDTO.getRequestedUserId());
        friendship.setRequester(requester);
        friendship.setRequestedUser(requestedUser);
        friendship.setStatus(FriendshipStatus.PENDING);
        return friendshipRepository.save(friendship);
    }

    @Override
    public Friendship findById(Long friendshipId) {
        return friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ResourceNotFoundException("Friendship", "friendshipId", friendshipId));
    }

    @Override
    public List<Friendship> findPendingFriendshipsByRequesterId(Long userId) {
        return friendshipRepository.findPendingFriendshipsByRequesterId(userId);
    }

    @Override
    @Transactional
    public void revokeFriendRequest(Long friendshipId) {
        Friendship friendship = findById(friendshipId);
        throwExceptionIfFriendshipIsNotPending(friendship);
        friendshipRepository.revokeFriendRequest(friendshipId);
    }

    @Override
    @Transactional
    public void acceptFriendRequest(Long friendshipId) {
        Friendship friendship = findById(friendshipId);
        throwExceptionIfFriendshipIsNotPending(friendship);
        friendshipRepository.acceptFriendRequest(friendshipId);

    }

    @Override
    @Transactional
    public void declineFriendRequest(Long friendshipId) {
        Friendship friendship = findById(friendshipId);
        throwExceptionIfFriendshipIsNotPending(friendship);
        friendshipRepository.declineFriendRequest(friendshipId);
    }

    @Override
    public List<Friendship> findAllReceivedPendingFriendRequests(Long userId) {
            return friendshipRepository.findAllReceivedPendingFriendRequests(userId);
    }

    @Override
    @Transactional
    public void unFriend(Long userId1, Long userId2) {
        friendshipRepository.unFriend(userId1, userId2);
    }


    public Optional<Friendship> findLatestFriendshipBetweenUsers(Long user1, Long user2) {
        return friendshipRepository.findLatestFriendship(user1, user2);
    }

    @Override
    public List<Friendship> findAcceptedFriendshipsByUserId(Long userId) {
        return friendshipRepository.findAcceptedFriendshipsByUserId(userId);
    }

    @Override
    public void throwExceptionIfFriendshipIsNotPending(Friendship friendship) {
        if(!FriendshipStatus.PENDING.equals(friendship.getStatus())) {
            throw new ChatAppAPIException(HttpStatus.BAD_REQUEST, "This friendship is not pending.");
        }
    }
}
