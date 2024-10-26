package org.example.services.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.example.services.QrCodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class QrCodeServiceImpl implements QrCodeService {
    @Value("${qr_code_url}")
    private String urlTemplate;

    private static final String QR_CODE_FOLDER_PATH = "src/main/resources/static/qr/";

    @Override
    public byte[] generateByteArray(Long volunteerId, Long eventId) {
        try {
            String url = urlTemplate.formatted(volunteerId, eventId);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public File generateFile(Long volunteerId, Long eventId) throws IOException {
        byte[] qrCodeByteArray = generateByteArray(volunteerId, eventId);

        ByteArrayInputStream bis = new ByteArrayInputStream(qrCodeByteArray);
        BufferedImage bImage2 = ImageIO.read(bis);

        File qrCodeImageFile = new File("v_%d_e_%d.jpg".formatted(volunteerId, eventId));
        ImageIO.write(bImage2, "jpg", qrCodeImageFile);

        return qrCodeImageFile;
    }
}


//TODO: orElseThrow()