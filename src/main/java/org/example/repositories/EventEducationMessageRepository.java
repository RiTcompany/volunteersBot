package org.example.repositories;

import org.example.entities.EventEducationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventEducationMessageRepository extends JpaRepository<EventEducationMessage, Long> {
    List<EventEducationMessage> findAllByEventIdOrderByMessageNumber(Long eventId);

    Optional<EventEducationMessage> findByEventIdAndMessageNumber(Long eventId, Integer messageNumber);
}
