package com.raytotti.convertcurrency.user.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserBuilder {

    private UUID id;
    private String cpf;
    private String name;

    public User build() {
        id = UUID.randomUUID();
        return new User(this);
    }

    public UserBuilder cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }
}
