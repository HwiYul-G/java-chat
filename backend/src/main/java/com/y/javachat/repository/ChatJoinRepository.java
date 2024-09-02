package com.y.javachat.repository;

import com.y.javachat.model.ChatJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatJoinRepository extends JpaRepository<ChatJoin, Long> {
}
