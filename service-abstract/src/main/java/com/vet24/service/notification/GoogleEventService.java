package com.vet24.service.notification;

import com.vet24.models.dto.googleEvent.GoogleEventDto;

import java.io.IOException;

public interface GoogleEventService {

    void createEvent(GoogleEventDto googleEventDto) throws IOException;
    void editEvent(GoogleEventDto googleEventDto) throws IOException;
    void deleteEvent(GoogleEventDto googleEventDto) throws IOException;
    String getRedirectUrl();
    void saveToken(String code, String user) throws IOException;


}