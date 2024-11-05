package org.example.services;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VolunteerEventService {
    List<Long> getEventVolunteerIdList(Long eventId);
}
