package org.example.repositories;

import org.example.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByStartTimeAfterAndIsAvailableIsTrue(Date date);

    default List<Event> findAllByStartTimeAfterTodayAndIsAvailableTrue() {
        return findAllByStartTimeAfterAndIsAvailableIsTrue(new Date());
    }

    @Query(value = "select e.resultsLink from Event as e where e.id = ?1")
    String findEventResultsLinkById(Long eventId);

    Event findByIdAndIsAvailableTrue(Long eventId);
}
