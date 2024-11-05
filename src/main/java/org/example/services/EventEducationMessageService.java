package org.example.services;

import org.example.entities.EventEducationMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventEducationMessageService {
    List<EventEducationMessage> getEventEducationMessageList(Long eventId);
}
