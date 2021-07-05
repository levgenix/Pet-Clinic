package com.vet24.models.user;

import com.vet24.models.enums.RoleNameEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = {"name"})
public class Role implements GrantedAuthority {

    @Id
    @NonNull
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(25)")
    @EqualsAndHashCode.Include
    private RoleNameEnum name;


    @Override
    public String getAuthority() {
        return name.toString();
    }
}
