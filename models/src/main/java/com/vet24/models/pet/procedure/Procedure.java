package com.vet24.models.pet.procedure;

import com.vet24.models.enums.ProcedureType;
import com.vet24.models.medicine.Medicine;
import com.vet24.models.notification.Notification;
import com.vet24.models.pet.Pet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Procedure implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column
    private LocalDate date;

    @Column(name = "type", nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    public ProcedureType type;

    @Column
    private String medicineBatchNumber;

    @Column
    private Boolean isPeriodical;

    @Column
    private Integer periodDays;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pet pet;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    protected Procedure(LocalDate date, ProcedureType type, String medicineBatchNumber,
                        Boolean isPeriodical, Integer periodDays, Medicine medicine, Pet pet) {
        this.date = date;
        this.type = type;
        this.medicineBatchNumber = medicineBatchNumber;
        this.isPeriodical = isPeriodical;
        this.periodDays = periodDays;
        this.medicine = medicine;
        this.pet = pet;
    }
}
