package com.y.javachat.repository;

import com.y.javachat.model.Friendship;
import com.y.javachat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findAllByUser(User user);
    Optional<Friendship> findByUserAndFriend(User user, User friend);
}
