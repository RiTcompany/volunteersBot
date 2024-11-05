package org.example.services;

import org.springframework.stereotype.Service;

@Service
public interface DiplomaService {
    String formDiploma(String fullName, String eventName, String date);

    void deleteDiplomaFile(String fileName);
}
