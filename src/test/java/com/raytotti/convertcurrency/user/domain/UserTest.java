package com.raytotti.convertcurrency.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private UserBuilder builder;

    @BeforeEach
    void beforeEach() {
        builder = User.builder()
                .cpf("111.111.111-11")
                .name("Ray Toti Felix de Araujo");
    }

    @Test
    void build() {
        User user = builder.build();

        assertNotNull(user.getId());
        assertEquals(builder.getCpf(), user.getCpf());
        assertEquals(builder.getName(), user.getName());
    }

    @Test
    void nullName() {
        builder.name(null);
        assertThrows(NullPointerException.class, builder::build);
    }

    @Test
    void nullCPF() {
        builder.cpf(null);
        assertThrows(NullPointerException.class, builder::build);
    }

    @Test
    void equalTest() {
        User user1 = builder.build();
        User user2 = builder.build();

        assertEquals(user1, user2);
    }

    @Test
    void notEqualTest() {
        User user1 = builder.build();
        User user2 = builder.cpf("000.000.000-00").build();

        assertNotEquals(user1, user2);
    }

}