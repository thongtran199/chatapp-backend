package com.springboot.chatapp.repository;

import com.springboot.chatapp.domain.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT f FROM Friendship f WHERE f.requester.userId = :userId AND f.status = 'ACCEPTED'")
    List<Friendship> findAllAcceptedFriends(@Param("userId") Long userId);

    @Query("SELECT f FROM Friendship f WHERE f.requester.userId = :requesterId AND f.requestedUser.userId = :requestedUserId AND f.status = 'ACCEPTED'")
    Optional<Friendship> findAcceptedFriendshipBetweenUsers(@Param("requesterId") Long requesterId, @Param("requestedUserId") Long requestedUserId);

    @Query("SELECT f FROM Friendship f WHERE f.requester.userId = :userId AND f.status = 'PENDING'")
    List<Friendship> findAllSentFriendRequests(@Param("userId") Long userId);

    @Query("SELECT f FROM Friendship f WHERE f.requester.userId = :userId AND f.status = 'DECLINED'")
    List<Friendship> findAllDeclinedFriendRequests(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Friendship f WHERE f.requester.userId = :requesterId AND f.requestedUser.userId = :requestedUserId AND f.status = 'PENDING'")
    void cancelFriendRequest(@Param("requesterId") Long requesterId, @Param("requestedUserId") Long requestedUserId);

    @Modifying
    @Query("UPDATE Friendship f SET f.status = 'ACCEPTED', f.updatedAt = CURRENT_TIMESTAMP WHERE f.requester.userId = :requesterId AND f.requestedUser.userId = :requestedUserId AND f.status = 'PENDING'")
    void acceptFriendRequest(@Param("requesterId") Long requesterId, @Param("requestedUserId") Long requestedUserId);

    @Modifying
    @Query("UPDATE Friendship f SET f.status = 'DECLINED', f.updatedAt = CURRENT_TIMESTAMP WHERE f.requester.userId = :requesterId AND f.requestedUser.userId = :requestedUserId AND f.status = 'PENDING'")
    void declineFriendRequest(@Param("requesterId") Long requesterId, @Param("requestedUserId") Long requestedUserId);

    @Query("SELECT f FROM Friendship f WHERE (f.requester.userId = :userId OR f.requestedUser.userId = :userId) AND f.status = 'ACCEPTED'")
    List<Friendship> findAllFriendsByUserId(@Param("userId") Long userId);
}
