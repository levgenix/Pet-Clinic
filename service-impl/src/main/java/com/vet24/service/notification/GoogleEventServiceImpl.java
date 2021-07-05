package com.vet24.service.notification;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import com.vet24.models.dto.googleEvent.GoogleEventDto;
import com.vet24.models.exception.CredentialException;
import com.vet24.models.exception.EventException;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

@Service
public class GoogleEventServiceImpl implements GoogleEventService {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);

    private final String CALLBACK_URI = "http://localhost:8080/oauth";
    private final String gdSecretKeys = "/credentials.json";
    private final String credentialsFolder = "tokens";
    private GoogleAuthorizationCodeFlow flow;

    /**
     * Inizializaciya potoka avtorizacii v google
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(GoogleEventServiceImpl.class.getResourceAsStream(gdSecretKeys)));
        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(credentialsFolder))).build();
    }

    /**
     *
     * @return vozvrawaet URL, na kotorii proishodit redirect
     */
    @Override
    public String getRedirectUrl() {
        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        String redirectURL = url.setRedirectUri(CALLBACK_URI).setAccessType("offline").build();
        return redirectURL;
    }

    /**
     *
     * @param code kod k tokenu, prihodyawii ot google
     * @param user pol'zovatel, k kotoromu sohranit' token avtorizacii
     * @throws IOException
     */
    @Override
    public void saveToken(String code, String user) throws IOException {
        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(CALLBACK_URI).execute();
        flow.createAndStoreCredential(response, user);
    }

    /**
     * Inicializaciya kalendarya
     * @param credential avtorizacionnyi token polzovatelya, k kalendaru kotorogo obrawaemsya
     * @return obiekt kalendar
     */
    private Calendar buildCalendar(Credential credential) {
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("Petclinic").build();
    }

    /**
     *
     * @param timestamp iz DTO
     * @return vremya v formate google sobitiya
     */
    private EventDateTime getTime(Timestamp timestamp) {
        DateTime dateTime = new DateTime(timestamp);
        EventDateTime time = new EventDateTime()
                .setDateTime(dateTime)
                .setTimeZone("Europe/Moscow");
        return time;
    }

    /**
     * create event in user's google calendar
     * @param googleEventDto
     * @throws CredentialException
     * @throws EventException
     */
    @Override
    public void createEvent(GoogleEventDto googleEventDto) throws CredentialException, EventException {
        Credential credential;
        try {
            credential = flow.loadCredential(googleEventDto.getEmail());
        } catch (IOException e) {
            throw new CredentialException("dont have credential for this user", googleEventDto.getEmail());
        }
        Event event = new Event()
                .setSummary(String.valueOf(googleEventDto.getSummary()))
                .setLocation(googleEventDto.getLocation())
                .setDescription(googleEventDto.getDescription());

        event.setStart(getTime(googleEventDto.getStartDate()));

        event.setEnd(getTime(googleEventDto.getEndDate()));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(30),
        };

        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail(googleEventDto.getEmail()),
        };
        event.setAttendees(Arrays.asList(attendees));
        try {
            event = buildCalendar(credential).events().insert("primary", event)
                    .setSendNotifications(true).execute();
        } catch (IOException e) {
            throw new EventException("cannot create event");
        }
        googleEventDto.setId(event.getId());
    }

    /**
     * edit event in user's google calendar by googleEventDto.id
     * @param googleEventDto
     * @throws IOException
     */
    @Override
    public void editEvent(GoogleEventDto googleEventDto) throws IOException {
        Credential credential;

        if (googleEventDto.getId() == null) {
            throw new EventException("cannot delete event, event id cannot be empty");
        }
        if (googleEventDto.getEmail() == null) {
            throw new EventException("cannot delete event, user email cannot be empty");
        }

        try {
            credential = flow.loadCredential(googleEventDto.getEmail());
        } catch (IOException e) {
            throw new CredentialException("dont have credential for this user", googleEventDto.getEmail());
        }

        Event changes = new Event().setSummary(googleEventDto.getSummary())
                .setDescription(googleEventDto.getDescription())
                .setLocation(googleEventDto.getLocation())
                .setStart(getTime(googleEventDto.getStartDate()))
                .setEnd(getTime(googleEventDto.getEndDate()));
        try {
            buildCalendar(credential).events().patch("primary",
                    googleEventDto.getId(), changes).execute();
        } catch (IOException e) {
            throw new EventException("cannot change event");
        }
    }

    /**
     * delete event from user's google calendar by googleEventDto.id
     * @param googleEventDto
     * @throws IOException
     */
    @Override
    public void deleteEvent(GoogleEventDto googleEventDto) throws IOException {
        Credential credential;

        if (googleEventDto.getId() == null) {
            throw new EventException("cannot delete event, event id cannot be empty");
        }
        if (googleEventDto.getEmail() == null) {
            throw new EventException("cannot delete event, user email cannot be empty");
        }

        try {
            credential = flow.loadCredential(googleEventDto.getEmail());
        } catch (IOException e) {
            throw new CredentialException("dont have credential for this user", googleEventDto.getEmail());
        }

        try {
            buildCalendar(credential).events().delete("primary", googleEventDto.getId()).execute();
        } catch (IOException e) {
            throw new EventException("cannot delete event");
        }
    }

}
