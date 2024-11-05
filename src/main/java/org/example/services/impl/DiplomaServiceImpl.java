package org.example.services.impl;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import org.example.services.DiplomaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DiplomaServiceImpl implements DiplomaService {
    @Value("${diploma.template}")
    private String diplomaTemplate;
    @Value("${diploma.fullName}")
    private String diplomaFullName;
    @Value("${diploma.eventName}")
    private String diplomaEventName;
    @Value("${diploma.date}")
    private String diplomaDate;
    @Value("${diploma.template.docx}")
    private String diplomaSource;

    @Override
    public String formDiploma(String fullName, String eventName, String date) {
        String diplomaDocxName = diplomaSource.formatted(System.currentTimeMillis());

        Document document = new Document();
        document.loadFromFile(diplomaTemplate);
        document.replace(diplomaFullName, fullName, false, true);
        document.replace(diplomaEventName, eventName, false, true);
        document.replace(diplomaDate, date, false, true);
        document.saveToFile(diplomaDocxName, FileFormat.PDF);

        return diplomaDocxName;
    }

    @Override
    public void deleteDiplomaFile(String fileName) {
        new File(fileName).delete();
    }
}
