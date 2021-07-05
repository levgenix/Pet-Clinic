package com.vet24.models.user;

import com.vet24.models.medicine.Diagnosis;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("DOCTOR")
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Doctor extends User {

    @OneToMany(
            mappedBy = "doctor",
            cascade = CascadeType.ALL
    )
    private List<Diagnosis> diagnoses = new ArrayList<>();


    public Doctor() {
        super();
    }

    public Doctor(String firstname, String lastname, String email, String password, Role role) {
        super(firstname, lastname, email, password, role);
    }
}
