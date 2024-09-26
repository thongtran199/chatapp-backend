package com.springboot.chatapp.service.impl;

import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import com.springboot.chatapp.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.FriendshipRepository;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Friendship save(FriendshipRequestDTO friendshipRequestDTO) {
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
    public List<Friendship> findAllAcceptedFriends(Long userId) {
        return friendshipRepository.findAllAcceptedFriends(userId);
    }

    @Override
    public List<Friendship> findAllSentFriendRequests(Long userId) {
        return friendshipRepository.findAllSentFriendRequests(userId);
    }

    @Override
    public List<Friendship> findAllDeclinedFriendRequests(Long userId) {
        return friendshipRepository.findAllDeclinedFriendRequests(userId);
    }

    @Override
    public void cancelFriendRequest(Long requesterId, Long requestedUserId) {
        friendshipRepository.cancelFriendRequest(requesterId, requestedUserId);
    }

    @Override
    public void acceptFriendRequest(Long requesterId, Long requestedUserId) {
        friendshipRepository.acceptFriendRequest(requesterId, requestedUserId);

    }

    @Override
    public void declineFriendRequest(Long requesterId, Long requestedUserId) {
        friendshipRepository.declineFriendRequest(requesterId, requestedUserId);
    }

    @Override
    public Friendship findAcceptedFriendshipBetweenUsers(Long requesterId, Long requestedUserId) {
        return friendshipRepository.findAcceptedFriendshipBetweenUsers(requesterId, requestedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Friendship", "requester_id and requested_user_id", requesterId + " and "+ requestedUserId));
    }

    @Override
    public boolean existsById(Long friendshipId) {
        return friendshipRepository.existsById(friendshipId);
    }

    @Override
    public List<Friendship> findAllFriendsByUserId(Long userId) {
        return friendshipRepository.findAllFriendsByUserId(userId);
    }


}
