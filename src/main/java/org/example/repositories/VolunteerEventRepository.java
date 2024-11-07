package org.example.repositories;

import org.example.entities.VolunteerEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerEventRepository extends JpaRepository<VolunteerEvent, Long> {
    List<VolunteerEvent> findAllByEventIdAndIsHereTrue(long eventId);

    boolean existsByVolunteerIdAndEventId(Long volunteerId, Long eventId);
}
