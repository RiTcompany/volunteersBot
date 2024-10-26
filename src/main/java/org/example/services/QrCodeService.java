package org.example.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public interface QrCodeService {
    byte[] generateByteArray(Long volunteerId, Long eventId);

    File generateFile(Long volunteerId, Long eventId) throws IOException;
}
