package com.springboot.chatapp.repository;

import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {


    @Query("SELECT f FROM Friendship f WHERE f.requester.userId = :requesterId AND f.requestedUser.userId = :requestedUserId")
    Optional<Friendship> findFriendshipByRequesterIdAndRequestedUserId(@Param("requesterId") Long requesterId, @Param("requestedUserId") Long requestedUserId);

    @Query("SELECT f FROM Friendship f WHERE f.requester.userId = :userId AND f.status = 'PENDING'")
    List<Friendship> findAllSentFriendRequests(@Param("userId") Long userId);

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

    @Query("SELECT f FROM Friendship f WHERE f.requestedUser.userId = :userId AND f.status = 'PENDING'")
    List<Friendship> findAllReceivedPendingFriendRequests(@Param("userId") Long userId);
    @Query("SELECT f FROM Friendship f WHERE (f.requester.userId = :userId1 AND f.requestedUser.userId = :userId2) OR (f.requester.userId = :userId2 AND f.requestedUser.userId = :userId1)")
    Optional<Friendship> getFriendshipBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);



}
