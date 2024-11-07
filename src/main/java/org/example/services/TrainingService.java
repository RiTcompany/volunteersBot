package org.example.services;

import org.example.entities.Event;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public interface TrainingService {

    Event addTrainingLink(Long eventId, String trainingLink);

    Event addResultsLink(Long eventId, String resultsLink);

    Boolean getResultByEmail(Long eventId, String email) throws GeneralSecurityException, IOException;

}
