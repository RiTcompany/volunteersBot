package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.EventEducationMessage;
import org.example.repositories.EventEducationMessageRepository;
import org.example.services.EventEducationMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventEducationMessageServiceImpl implements EventEducationMessageService {
    private final EventEducationMessageRepository eventEducationMessageRepository;

    @Override
    public List<EventEducationMessage> getEventEducationMessageList(Long eventId) {
        return eventEducationMessageRepository.findAllByEventIdOrderByMessageNumber(eventId);
    }
}
