package com.springboot.chatapp.service.impl;

import com.springboot.chatapp.domain.dto.user.request.FriendshipRequestDTO;
import com.springboot.chatapp.domain.dto.user.response.FoundUserResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.FriendshipFoundUserResponseDTO;
import com.springboot.chatapp.domain.dto.user.response.UserResponseDTO;
import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import com.springboot.chatapp.exception.FriendshipStatusNotIsPendingException;
import com.springboot.chatapp.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.FriendshipRepository;
import com.springboot.chatapp.service.FriendshipService;
import com.springboot.chatapp.service.UserService;
import com.springboot.chatapp.utils.FriendshipUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Friendship sendFriendRequest(FriendshipRequestDTO friendshipRequestDTO) {
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
        if(friendship.getStatus() != FriendshipStatus.PENDING)
        {
            throw new FriendshipStatusNotIsPendingException(friendshipId, friendship.getStatus());
        }
        friendshipRepository.revokeFriendRequest(friendshipId);
    }

    @Override
    @Transactional
    public void acceptFriendRequest(Long friendshipId) {
        Friendship friendship = findById(friendshipId);
        if(friendship.getStatus() != FriendshipStatus.PENDING)
        {
            throw new FriendshipStatusNotIsPendingException(friendshipId, friendship.getStatus());
        }
        friendshipRepository.acceptFriendRequest(friendshipId);

    }

    @Override
    @Transactional
    public void declineFriendRequest(Long friendshipId) {
        Friendship friendship = findById(friendshipId);
        if(friendship.getStatus() != FriendshipStatus.PENDING)
        {
            throw new FriendshipStatusNotIsPendingException(friendshipId, friendship.getStatus());
        }
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
}
