package com.vet24.models.pet;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import com.vet24.models.medicine.Diagnosis;
import com.vet24.models.notification.Notification;
import com.vet24.models.pet.reproduction.Reproduction;
import com.vet24.models.pet.procedure.Procedure;
import com.vet24.models.user.Client;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "pet_entities")
@DiscriminatorColumn(name = "pet_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String avatar;

    @Column(nullable = false)
    private LocalDate birthDay;

    @Column(insertable = false, updatable = false, name = "pet_type")
    @Enumerated(EnumType.STRING)
    private PetType petType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String breed;

    @Column
    private String color;

    @Column
    @Enumerated(EnumType.STRING)
    private PetSize petSize;

    @Column
    private Double weight;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Procedure> procedures = new ArrayList<>();

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Diagnosis> diagnoses = new ArrayList<>();


    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Reproduction> reproductions = new ArrayList<>();

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(
            mappedBy = "pet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PetFound> petFounds = new ArrayList<>();

    protected Pet() {
    }

    protected Pet(String name, LocalDate birthDay, Gender gender, String breed, Client client) {
        this.name = name;
        this.birthDay = birthDay;
        this.gender = gender;
        this.breed = breed;
        this.client = client;
    }

    protected Pet(String name, LocalDate birthDay, Gender gender, String breed, Client client,
                  List<Procedure> procedures, List<Reproduction> reproductions, List<Notification> notifications) {
        this(name, birthDay, gender, breed, client);
        this.procedures = procedures;
        this.reproductions = reproductions;
        this.notifications = notifications;
    }

    public void addProcedure(Procedure procedure) {
        procedures.add(procedure);
        procedure.setPet(this);
    }

    public void removeProcedure(Procedure procedure) {
        procedures.remove(procedure);
        procedure.setPet(null);
    }

    public void addReproduction(Reproduction reproduction){
        reproductions.add(reproduction);
        reproduction.setPet(this);
    }

    public void removeReproduction(Reproduction reproduction) {
        reproductions.remove(reproduction);
        reproduction.setPet(null);
    }

    public void addNotification(Notification notification){
        notifications.add(notification);
        notification.setPet(this);
    }

    public void removeNotification(Notification notification) {
        notifications.remove(notification);
        notification.setPet(null);
    }
}
