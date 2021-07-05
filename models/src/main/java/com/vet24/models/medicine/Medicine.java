package com.vet24.models.medicine;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"manufactureName" , "name"})})
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Medicine {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String manufactureName;

    @Column(nullable = false)
    private String name;

    private String icon;

    @Column(nullable = false)
    private String description;

    public Medicine(String manufactureName, String name, String icon, String description) {
        this.manufactureName = manufactureName;
        this.name = name;
        this.icon = icon;
        this.description = description;
    }
}
