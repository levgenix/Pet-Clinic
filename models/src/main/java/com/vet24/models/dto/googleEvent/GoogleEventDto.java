package com.vet24.models.dto.googleEvent;

import java.sql.Timestamp;

import lombok.Data;

/**
 * GoogleEventDto
 * Форма для создания, редактирования, удаления события в google календаре
 *
 */
@Data
public class GoogleEventDto {
    /**
     * Поле id, получаемое после создания создания события в календаре
     */
    String id;
    /**
     * Zagolovok
     */
    String summary;
    /**
     * Lokaciya vstrechi
     */
    String location;
    /**
     * Opisanie vstrechi
     */
    String description;
    /**
     * Vremya nachala i okonchaniya sobitiya
     */
    Timestamp startDate;
    Timestamp endDate;
    /**
     * Email polzovatelya, v chei kalendar hotim otpravit sobitie
     */
    String email;
}
