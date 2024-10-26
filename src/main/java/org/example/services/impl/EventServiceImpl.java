package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Event;
import org.example.repositories.EventRepository;
import org.example.services.EventService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public boolean existsEvent(Long eventId) {
        return eventRepository.existsById(eventId);
    }

    @Override
    public Event getById(Long eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    @Override
    public List<Event> getFutureAll() {
        return eventRepository.findAllByStartTimeAfterToday();
    }
}
