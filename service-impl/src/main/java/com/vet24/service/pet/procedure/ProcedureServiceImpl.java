package com.vet24.service.pet.procedure;

import com.vet24.dao.pet.procedure.ProcedureDao;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.notification.Notification;
import com.vet24.models.pet.Pet;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.service.ReadWriteServiceImpl;
import com.vet24.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ProcedureServiceImpl extends ReadWriteServiceImpl<Long, Procedure> implements ProcedureService {

    private final NotificationService notificationService;
    private final ProcedureDao procedureDao;

    @Autowired
    public ProcedureServiceImpl(ProcedureDao procedureDao,
                                NotificationService notificationService) {
        super(procedureDao);
        this.notificationService = notificationService;
        this.procedureDao = procedureDao;
    }

    @Override
    public void persist(Procedure procedure) {
        if (procedure.getIsPeriodical()) {
            persistProcedureNotification(procedure);
        }

        procedureDao.persist(procedure);
    }

    @Override
    public Procedure update(Procedure procedure) {
        Procedure oldEntity = super.getByKey(procedure.getId());

        // old(not periodical) + new(periodical) -> create notification & event
        if (!oldEntity.getIsPeriodical() && procedure.getIsPeriodical()) {
            persistProcedureNotification(procedure);
        }
        // old(periodical) + new(periodical) -> update notification & event
        if (oldEntity.getIsPeriodical() && procedure.getIsPeriodical()) {
            updateProcedureNotification(oldEntity, procedure);
        }
        // old(periodical) + new(not periodical) -> delete notification & event
        if (oldEntity.getIsPeriodical() && !procedure.getIsPeriodical()) {
            deleteProcedureNotification(oldEntity);
        }

        return procedureDao.update(procedure);
    }

    @Override
    public void delete(Procedure procedure) {
        if (procedure.getIsPeriodical()) {
            deleteProcedureNotification(procedure);
        }

        procedureDao.delete(procedure);
    }

    private void persistProcedureNotification(Procedure procedure) {
        if (!(procedure.getPeriodDays() > 0)) {
            throw new BadRequestException("for periodical procedure need to set period days");
        }
        Pet pet = procedure.getPet();
        Notification notification = new Notification(
                null,
                null,
                Timestamp.valueOf(LocalDateTime.of(procedure.getDate().plusDays(procedure.getPeriodDays()), LocalTime.MIDNIGHT)),
                Timestamp.valueOf(LocalDateTime.of(procedure.getDate().plusDays(procedure.getPeriodDays()), LocalTime.NOON)),
                "Periodic procedure for your pet",
                "Pet clinic 1",
                "Procedure '" + procedure.getType().name().toLowerCase() + "' \n" +
                        "for pet " + pet.getName() + " \n" +
                        "[every " + procedure.getPeriodDays() + " day(s)]",
                pet
        );
        notificationService.persist(notification);

        procedure.setNotification(notification);
        pet.addNotification(notification);
    }

    private void updateProcedureNotification(Procedure oldProcedure, Procedure newProcedure) {
        if (!(newProcedure.getPeriodDays() > 0)) {
            throw new BadRequestException("for periodical procedure need to set period days");
        }

        if (oldProcedure.getNotification() != null) {
            Pet pet = oldProcedure.getPet();

            Notification notification = new Notification(
                    oldProcedure.getNotification().getId(),
                    oldProcedure.getNotification().getEventId(),
                    Timestamp.valueOf(LocalDateTime.of(newProcedure.getDate().plusDays(newProcedure.getPeriodDays()), LocalTime.MIDNIGHT)),
                    Timestamp.valueOf(LocalDateTime.of(newProcedure.getDate().plusDays(newProcedure.getPeriodDays()), LocalTime.NOON)),
                    "Periodic procedure for your pet",
                    "Pet clinic 1",
                    "Procedure '" + newProcedure.getType().name().toLowerCase() + "' \n" +
                            "for pet " + pet.getName() + " \n" +
                            "[every " + newProcedure.getPeriodDays() + " day(s)]",
                    pet
            );

            notificationService.update(notification);
            newProcedure.setNotification(notification);
        }
    }

    private void deleteProcedureNotification(Procedure procedure) {
        if (procedure.getNotification() != null) {
            Notification notification = procedure.getNotification();
            notificationService.delete(notification);
            procedure.setNotification(null);
            procedure.getPet().removeNotification(notification);
        }
    }
}
