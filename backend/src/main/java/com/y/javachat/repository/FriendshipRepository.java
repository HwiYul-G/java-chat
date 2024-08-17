package com.y.javachat.repository;

import com.y.javachat.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship,Long> {

    List<Friendship> findAllByUserId(Long userId);

    Optional<Friendship> findByUserIdAndAndFriendId(Long userId, Long friendId);

}