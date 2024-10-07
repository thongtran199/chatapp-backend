package com.springboot.chatapp.repository;

import com.springboot.chatapp.TestDataUtils;
import com.springboot.chatapp.model.entity.Friendship;
import com.springboot.chatapp.model.entity.User;
import com.springboot.chatapp.model.enums.FriendshipStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FriendshipRepositoryTest {
    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User requester;
    private User requestedUser;
    private Friendship friendship;

    @BeforeEach
    void setUp() {
        requester = TestDataUtils.createUser("thongtran", "thongtran@gmail.com", "Tran Van Thong");
        requestedUser = TestDataUtils.createUser("khanhvan", "khanhvan@gmail.com", "Tran Nguyen Khanh Van");
        userRepository.save(requester);
        userRepository.save(requestedUser);

        friendship = TestDataUtils.createFriendship(requester, requestedUser, FriendshipStatus.PENDING);
        friendshipRepository.save(friendship);
    }

    @AfterEach
    void tearDown() {
        friendshipRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findPendingFriendshipsByRequesterId() {
        List<Friendship> pendingFriendships = friendshipRepository.findPendingFriendshipsByRequesterId(requester.getUserId());
        assertThat(pendingFriendships).hasSize(1);
        assertThat(pendingFriendships.get(0).getStatus()).isEqualTo(FriendshipStatus.PENDING);
    }

    @Test
    void revokeFriendRequest() {
        friendshipRepository.revokeFriendRequest(friendship.getFriendshipId());

        entityManager.flush();
        entityManager.clear();

        Friendship updatedFriendship = friendshipRepository.findById(friendship.getFriendshipId()).orElseThrow();
        assertThat(updatedFriendship.getStatus()).isEqualTo(FriendshipStatus.REVOKED);
    }

    @Test
    void unFriend() {
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);

        friendshipRepository.unFriend(requester.getUserId(), requestedUser.getUserId());

        entityManager.flush();
        entityManager.clear();

        Friendship updatedFriendship = friendshipRepository.findById(friendship.getFriendshipId()).orElseThrow();
        assertThat(updatedFriendship.getStatus()).isEqualTo(FriendshipStatus.UNFRIEND);
    }

    @Test
    void acceptFriendRequest() {
        friendshipRepository.acceptFriendRequest(friendship.getFriendshipId());

        entityManager.flush();
        entityManager.clear();

        Friendship updatedFriendship = friendshipRepository.findById(friendship.getFriendshipId()).orElseThrow();
        assertThat(updatedFriendship.getStatus()).isEqualTo(FriendshipStatus.ACCEPTED);
    }

    @Test
    void declineFriendRequest() {
        friendshipRepository.declineFriendRequest(friendship.getFriendshipId());

        entityManager.flush();
        entityManager.clear();

        Friendship updatedFriendship = friendshipRepository.findById(friendship.getFriendshipId()).orElseThrow();
        assertThat(updatedFriendship.getStatus()).isEqualTo(FriendshipStatus.DECLINED);
    }

    @Test
    void findAcceptedFriendshipsByUserId() {
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);

        entityManager.flush();
        entityManager.clear();

        List<Friendship> acceptedFriendships = friendshipRepository.findAcceptedFriendshipsByUserId(requester.getUserId());
        assertThat(acceptedFriendships).hasSize(1);
    }

    @Test
    void findAllReceivedPendingFriendRequests() {
        List<Friendship> receivedRequests = friendshipRepository.findAllReceivedPendingFriendRequests(requestedUser.getUserId());
        assertThat(receivedRequests).hasSize(1);
    }

    @Test
    void findLatestFriendship() {
        Friendship newFriendship = TestDataUtils.createFriendship(requester, requestedUser, FriendshipStatus.ACCEPTED);
        friendshipRepository.save(newFriendship);

        Friendship latestFriendship = friendshipRepository.findLatestFriendship(requester.getUserId(), requestedUser.getUserId()).orElseThrow();
        assertThat(latestFriendship.getStatus()).isEqualTo(newFriendship.getStatus());
    }
}