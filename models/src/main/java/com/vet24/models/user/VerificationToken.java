package com.vet24.models.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VerificationToken  implements Serializable {
    @Id
    @NonNull
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @OneToOne(targetEntity = Client.class,cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private Client client;

}