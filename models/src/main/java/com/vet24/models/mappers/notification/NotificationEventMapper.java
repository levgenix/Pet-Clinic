package com.vet24.models.mappers.notification;

import com.vet24.models.dto.googleEvent.GoogleEventDto;
import com.vet24.models.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationEventMapper {

    @Mapping(source = "notification.eventId", target = "id")
    @Mapping(source = "email", target = "email")
    GoogleEventDto notificationToGoogleEventDto(Notification notification, String email);
}
