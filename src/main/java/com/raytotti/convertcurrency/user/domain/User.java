package com.raytotti.convertcurrency.user.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(name = "user_uk", columnNames = "cpf")
})
public final class User {

    @Id
    private UUID id;

    @NotNull
    @Column(unique = true)
    private String cpf;

    @NotNull
    private String name;

    User(UserBuilder builder) {
        this.id = Objects.requireNonNull(builder.getId());
        this.cpf = Objects.requireNonNull(builder.getCpf());
        this.name = Objects.requireNonNull(builder.getName());
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(cpf, user.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }
}
