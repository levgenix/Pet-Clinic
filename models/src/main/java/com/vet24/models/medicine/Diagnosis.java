package com.vet24.models.medicine;

import com.vet24.models.pet.Pet;
import com.vet24.models.user.Doctor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@RequiredArgsConstructor
public class Diagnosis implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    protected Doctor doctor;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    protected Pet pet;

    @NonNull
    @Column(nullable = false)
    private String description;

}
