package com.springboot.chatapp.repository;

import com.springboot.chatapp.domain.entity.Friendship;
import com.springboot.chatapp.domain.entity.User;
import com.springboot.chatapp.domain.enumerate.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT f FROM Friendship f WHERE f.friendshipId IN (" +
            "SELECT MAX(f2.friendshipId) FROM Friendship f2 " +
            "WHERE f2.requester.userId = :userId " +
            "GROUP BY f2.requestedUser.userId) " +
            "AND f.status = 'PENDING'")
    List<Friendship> findPendingFriendshipsByRequesterId(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Friendship f SET f.status = 'REVOKED', f.updatedAt = CURRENT_TIMESTAMP WHERE f.friendshipId = :friendshipId")
    void revokeFriendRequest(@Param("friendshipId") Long friendshipId);

    @Modifying
    @Query("UPDATE Friendship f SET f.status = 'UNFRIEND', f.updatedAt = CURRENT_TIMESTAMP WHERE ((f.requester.userId = :user1Id AND f.requestedUser.userId = :user2Id) OR (f.requester.userId = :user2Id AND f.requestedUser.userId = :user1Id)) AND f.status = 'ACCEPTED'")
    void unFriend(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    @Modifying
    @Query("UPDATE Friendship f SET f.status = 'ACCEPTED', f.updatedAt = CURRENT_TIMESTAMP WHERE f.friendshipId = :friendshipId")
    void acceptFriendRequest(@Param("friendshipId") Long friendshipId);

    @Modifying
    @Query("UPDATE Friendship f SET f.status = 'DECLINED', f.updatedAt = CURRENT_TIMESTAMP WHERE f.friendshipId = :friendshipId")
    void declineFriendRequest(@Param("friendshipId") Long friendshipId);

    @Query("SELECT f FROM Friendship f WHERE f.friendshipId IN (" +
            "SELECT MAX(f2.friendshipId) FROM Friendship f2 " +
            "WHERE f2.requester.userId = :userId OR f2.requestedUser.userId = :userId " +
            "GROUP BY CASE " +
            "WHEN f2.requester.userId = :userId THEN f2.requestedUser.userId " +
            "ELSE f2.requester.userId " +
            "END) " +
            "AND f.status = 'ACCEPTED'")
    List<Friendship> findAcceptedFriendshipsByUserId(@Param("userId") Long userId);

    @Query("SELECT f FROM Friendship f WHERE f.requestedUser.userId = :userId AND f.status = 'PENDING'")
    List<Friendship> findAllReceivedPendingFriendRequests(@Param("userId") Long userId);

    @Query("SELECT f FROM Friendship f WHERE f.friendshipId = (" +
            "SELECT MAX(f2.friendshipId) FROM Friendship f2 " +
            "WHERE (f2.requester.userId = :user1 AND f2.requestedUser.userId = :user2) " +
            "OR (f2.requester.userId = :user2 AND f2.requestedUser.userId = :user1))")
    Optional<Friendship> findLatestFriendship(@Param("user1") Long user1, @Param("user2") Long user2);



}
