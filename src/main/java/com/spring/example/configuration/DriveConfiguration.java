package com.spring.example.configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.auth.oauth2.ComputeEngineCredentials;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class DriveConfiguration {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static Drive drive;

    static {
        final NetHttpTransport HTTP_TRANSPORT;
        try {
            GoogleCredential credential = GoogleCredential.getApplicationDefault();
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("ESOMAR application")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Drive getDrive() {
        return drive;
    }
}
