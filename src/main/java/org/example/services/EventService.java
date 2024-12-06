package org.example.services;

import org.example.entities.Event;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface EventService {
    boolean existsEvent(Long eventId);

    Event getById(Long eventId);

    Event getAvailableById(Long eventId);

    List<Event> getFutureAll();
}
