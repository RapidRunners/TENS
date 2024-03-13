package az.code.tensapi.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;

@Service
public class GoogleCalendarService {
    private final NetHttpTransport httpTransport;
    private final JsonFactory jsonFactory;
    private static final String APPLICATION_NAME = "TaskManagerApp";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    public GoogleCalendarService() throws IOException, GeneralSecurityException {
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.jsonFactory = JacksonFactory.getDefaultInstance();
    }

    public void createEvent(String userEmail, String summary, String description, LocalDate deadline) throws IOException, GeneralSecurityException {
        if (deadline == null) {
            throw new IllegalArgumentException("Deadline cannot be null");
        }

        Calendar service = new Calendar.Builder(httpTransport, jsonFactory, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME)
                .build();


        try {
            Event event = new Event()
                    .setSummary(summary)
                    .setDescription(description);

            DateTime startDateTime = new DateTime(deadline.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("UTC");
            event.setStart(start);
            event.setEnd(start);

            String calendarId = "primary";
            service.events().insert(calendarId, event).execute();

            System.out.println("Event created successfully");
        } catch (IOException e) {
            System.err.println("Error creating event: " + e.getMessage());
            throw e;
        }
    }


    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets JSON file (typically generated from the Google Developers Console)
        InputStream in = GoogleCalendarService.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            System.out.println("credentials.json not found");
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Collections.singletonList(CalendarScopes.CALENDAR))
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(3000).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
