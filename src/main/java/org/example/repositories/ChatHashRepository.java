package org.example.repositories;

import org.example.entities.ChatHash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatHashRepository extends JpaRepository<ChatHash, Long> {
    Optional<ChatHash> findByChatId(long chatId);
}
