package org.example.repositories;

import org.example.entities.Event;
import org.example.entities.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Optional<Volunteer> findByChatId(long chatId);

    boolean existsByChatId(long chatId);

    List<Volunteer> findAllByEventListContains(Event event);

    @Query(value = "select v.id from Volunteer as v where v.email = ?1")
    Long findByEmail(String email);

    Optional<Volunteer> findByVolunteerId(Long volunteerId);

    boolean existsByEmail(String email);
}
