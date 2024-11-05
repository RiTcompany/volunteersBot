package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.VolunteerEvent;
import org.example.repositories.VolunteerEventRepository;
import org.example.services.VolunteerEventService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VolunteerEventServiceImpl implements VolunteerEventService {
    private final VolunteerEventRepository volunteerEventRepository;

    @Override
    public List<Long> getEventVolunteerIdList(Long eventId) {
        return volunteerEventRepository.findAllByEventIdAndIsHereTrue(eventId).stream()
                .map(VolunteerEvent::getVolunteerId)
                .toList();
    }
}
