package org.example.services.impl;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.Event;
import org.example.entities.EventEducationMessage;
import org.example.entities.VolunteerEvent;
import org.example.enums.EMessage;
import org.example.exceptions.EventNotFoundException;
import org.example.repositories.EventEducationMessageRepository;
import org.example.repositories.EventRepository;
import org.example.repositories.VolunteerEventRepository;
import org.example.repositories.VolunteerRepository;
import org.example.services.TrainingService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final EventRepository eventRepository;
    private final EventEducationMessageRepository eventEducationMessageRepository;
    private final VolunteerEventRepository volunteerEventRepository;
    private final VolunteerRepository volunteerRepository;

    @Override
    public Event addTrainingLink(Long eventId, String trainingLink) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId.toString()));
        event.setTrainingLink(trainingLink);
        eventEducationMessageRepository.save(
                new EventEducationMessage(eventId, 6, event.getTrainingLink(), EMessage.TEXT));
        return eventRepository.save(event);
    }

    @Override
    public Event addResultsLink(Long eventId, String resultsLink) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId.toString()));
        event.setResultsLink(resultsLink);
        return eventRepository.save(event);
    }

    @Override
    public Boolean getResultByEmail(Long eventId, String email) throws IOException {
        Long volunteerId = volunteerRepository.findByEmail(email);

        if (!volunteerEventRepository.existsByVolunteerIdAndEventId(volunteerId, eventId)) {

            VolunteerEvent volunteerEvent = new VolunteerEvent(volunteerId, eventId);

            String spreadsheetUrl = eventRepository.findEventResultsLinkById(eventId);

            String spreadsheetId = extractSpreadsheetId(spreadsheetUrl);
            if (spreadsheetId == null) {
                throw new IllegalArgumentException("Invalid Google Sheets URL.");
            }

            String range = "Sheet1!B1:C";

            String url = String.format(
                    "https://sheets.googleapis.com/v4/spreadsheets/%s/values/%s?key=AIzaSyDfzw8ubHYAvVKDL5MK6VObX9Kq3eZdO80",
                    spreadsheetId, range
            );

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                log.info("Failed to retrieve data. Response code: {}", responseCode);
                return null;
            }

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            ValueRange response = new Gson().fromJson(reader, ValueRange.class);
            List<List<Object>> values = response.getValues();

            if (values == null || values.isEmpty()) {
                log.info("No data found.");
                return null;
            }

            for (List<Object> row : values) {
                if (row.size() > 0 && row.get(0).toString().equalsIgnoreCase(email)) {
                    if (row.size() > 1 && areNumbersEqual(row.get(1).toString())) {
                        volunteerEvent.setIsTrainingPassed(true);
                        volunteerEventRepository.save(volunteerEvent);
                        return true;
                    }
                }
            }


            log.info("Email not found in the sheet.");
            return false;
        } else {
            return true;
        }
    }

    private String extractSpreadsheetId(String url) {
        Pattern pattern = Pattern.compile("/spreadsheets/d/([a-zA-Z0-9-_]+)");
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }

    public boolean areNumbersEqual(String input) {
        if (input == null || !input.contains("/")) {
            return false;
        }
        input = input.replaceAll("\\s+", "");
        String[] parts = input.split("/");
        if (parts.length != 2) {
            return false;
        }
        try {
            int number1 = Integer.parseInt(parts[0]);
            int number2 = Integer.parseInt(parts[1]);
            return number1 == number2;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
