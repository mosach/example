package com.spring.example.configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class DriveConfiguration {

    private static final Logger logger = LoggerFactory.getLogger("DriveConfiguration");
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static Drive drive;

    static {
        createNewDrive();
    }

    private static void createNewDrive() {
        final NetHttpTransport HTTP_TRANSPORT;
        try {
            GoogleCredential credential =
                    GoogleCredential.fromStream(new FileInputStream("/home/toolkitgdpr/Platform.json")).createScoped(Collections.singleton(DriveScopes.DRIVE));

            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, setHttpTimeout(credential))
                    .setApplicationName("ESOMAR application")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            logger.error("Failed Instantiation",e);
            e.printStackTrace();
        }
    }

    public Drive createDrive() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT;
        Drive drive = null;
        try {
            GoogleCredential credential =
                    GoogleCredential.fromStream(new FileInputStream("/home/toolkitgdpr/Platform.json")).createScoped(Collections.singleton(DriveScopes.DRIVE));

            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, setHttpTimeout(credential))
                    .setApplicationName("ESOMAR application")
                    .build();
            return drive;
        } catch (GeneralSecurityException | IOException e) {
            logger.error("Failed Instantiation",e);
            e.printStackTrace();
            throw e;
        }
    }

    private static HttpRequestInitializer setHttpTimeout(final HttpRequestInitializer requestInitializer) {
        return new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                requestInitializer.initialize(httpRequest);
                httpRequest.setConnectTimeout(10 * 60000);  // 10 minutes connect timeout
                httpRequest.setReadTimeout(10 * 60000);  // 10 minutes read timeout
            }
        };
    }

    public static Drive getDrive() {
        return drive;
    }
}
