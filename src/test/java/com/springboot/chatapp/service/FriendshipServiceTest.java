package com.springboot.chatapp.service;

import com.springboot.chatapp.model.dto.friendship.FriendshipRequestDto;
import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.enums.FriendshipStatus;
import com.springboot.chatapp.model.exception.ChatAppAPIException;
import com.springboot.chatapp.model.exception.ResourceNotFoundException;
import com.springboot.chatapp.repository.FriendshipRepository;
import com.springboot.chatapp.service.impl.FriendshipServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FriendshipServiceTest {

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private FriendshipServiceImpl friendshipService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendFriendRequest() {
        FriendshipRequestDto friendshipRequest = new FriendshipRequestDto();
        friendshipRequest.setRequesterId(1L);
        friendshipRequest.setRequestedUserId(2L);

        User requester = new User();
        requester.setUserId(1L);
        User requestedUser = new User();
        requestedUser.setUserId(2L);

        when(userService.findById(1L)).thenReturn(requester);
        when(userService.findById(2L)).thenReturn(requestedUser);
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(new Friendship());

        Friendship result = friendshipService.sendFriendRequest(friendshipRequest);

        assertNotNull(result);
        assertEquals(FriendshipStatus.PENDING, result.getStatus());
        verify(friendshipRepository, times(1)).save(any(Friendship.class));
    }

    @Test
    void findById() {
        Friendship friendship = new Friendship();
        friendship.setFriendshipId(1L);

        when(friendshipRepository.findById(1L)).thenReturn(Optional.of(friendship));

        Friendship result = friendshipService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getFriendshipId());
        verify(friendshipRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        when(friendshipRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> friendshipService.findById(1L));
        verify(friendshipRepository, times(1)).findById(1L);
    }

    @Test
    void findPendingFriendshipsByRequesterId() {
        when(friendshipRepository.findPendingFriendshipsByRequesterId(1L)).thenReturn(List.of(new Friendship()));

        List<Friendship> result = friendshipService.findPendingFriendshipsByRequesterId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(friendshipRepository, times(1)).findPendingFriendshipsByRequesterId(1L);
    }

    @Test
    void revokeFriendRequest() {
        Friendship friendship = new Friendship();
        friendship.setFriendshipId(1L);
        friendship.setStatus(FriendshipStatus.PENDING);

        when(friendshipRepository.findById(1L)).thenReturn(Optional.of(friendship));

        friendshipService.revokeFriendRequest(1L);

        verify(friendshipRepository, times(1)).revokeFriendRequest(1L);
    }

    @Test
    void revokeFriendRequest_NotPending() {
        Friendship friendship = new Friendship();
        friendship.setStatus(FriendshipStatus.ACCEPTED);

        when(friendshipRepository.findById(1L)).thenReturn(Optional.of(friendship));

        assertThrows(ChatAppAPIException.class, () -> friendshipService.revokeFriendRequest(1L));
        verify(friendshipRepository, never()).revokeFriendRequest(any());
    }

    @Test
    void acceptFriendRequest() {
        Friendship friendship = new Friendship();
        friendship.setStatus(FriendshipStatus.PENDING);

        when(friendshipRepository.findById(1L)).thenReturn(Optional.of(friendship));

        friendshipService.acceptFriendRequest(1L);

        verify(friendshipRepository, times(1)).acceptFriendRequest(1L);
    }

    @Test
    void acceptFriendRequest_NotPending() {
        Friendship friendship = new Friendship();
        friendship.setStatus(FriendshipStatus.ACCEPTED);

        when(friendshipRepository.findById(1L)).thenReturn(Optional.of(friendship));

        assertThrows(ChatAppAPIException.class, () -> friendshipService.acceptFriendRequest(1L));
        verify(friendshipRepository, never()).acceptFriendRequest(any());
    }

    @Test
    void declineFriendRequest() {
        Friendship friendship = new Friendship();
        friendship.setStatus(FriendshipStatus.PENDING);

        when(friendshipRepository.findById(1L)).thenReturn(Optional.of(friendship));

        friendshipService.declineFriendRequest(1L);

        verify(friendshipRepository, times(1)).declineFriendRequest(1L);
    }

    @Test
    void findAllReceivedPendingFriendRequests() {
        when(friendshipRepository.findAllReceivedPendingFriendRequests(1L)).thenReturn(List.of(new Friendship()));

        List<Friendship> result = friendshipService.findAllReceivedPendingFriendRequests(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(friendshipRepository, times(1)).findAllReceivedPendingFriendRequests(1L);
    }

    @Test
    void unFriend() {
        friendshipService.unFriend(1L, 2L);

        verify(friendshipRepository, times(1)).unFriend(1L, 2L);
    }

    @Test
    void findLatestFriendshipBetweenUsers() {
        when(friendshipRepository.findLatestFriendship(1L, 2L)).thenReturn(Optional.of(new Friendship()));

        Optional<Friendship> result = friendshipService.findLatestFriendshipBetweenUsers(1L, 2L);

        assertTrue(result.isPresent());
        verify(friendshipRepository, times(1)).findLatestFriendship(1L, 2L);
    }

    @Test
    void findAcceptedFriendshipsByUserId() {
        when(friendshipRepository.findAcceptedFriendshipsByUserId(1L)).thenReturn(List.of(new Friendship()));

        List<Friendship> result = friendshipService.findAcceptedFriendshipsByUserId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(friendshipRepository, times(1)).findAcceptedFriendshipsByUserId(1L);
    }

    @Test
    void throwExceptionIfFriendshipIsNotPending() {
        Friendship friendship = new Friendship();
        friendship.setStatus(FriendshipStatus.ACCEPTED);

        assertThrows(ChatAppAPIException.class, () -> friendshipService.throwExceptionIfFriendshipIsNotPending(friendship));
    }
}
