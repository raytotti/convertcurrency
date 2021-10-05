package com.raytotti.convertcurrency.user.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserRequestTest {

    private final String CPF = "430.609.538-05";
    private final String NAME = "Ray Toti Felix de Araujo";

    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void build() {
        CreateUserRequest createUserRequest = new CreateUserRequest(CPF, NAME);
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

        assertEquals(CPF, createUserRequest.getCpf());
        assertEquals(NAME, createUserRequest.getName());
        assertTrue(violations.isEmpty());
    }

    @Nested
    class ValidateName {

        @Test
        void nameNull() {
            CreateUserRequest createUserRequest = new CreateUserRequest(CPF, null);
            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

            assertEquals(CPF, createUserRequest.getCpf());
            assertFalse(violations.isEmpty());
        }

        @Test
        void nameEmpty() {
            CreateUserRequest createUserRequest = new CreateUserRequest(CPF, "");
            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

            assertEquals(CPF, createUserRequest.getCpf());
            assertFalse(violations.isEmpty());
        }

        @Test
        void nameMin() {
            CreateUserRequest createUserRequest = new CreateUserRequest(CPF, "ab");
            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

            assertEquals(CPF, createUserRequest.getCpf());
            assertFalse(violations.isEmpty());
        }

        @Test
        void nameMax() {
            String name = "A".repeat(257);
            CreateUserRequest createUserRequest = new CreateUserRequest(CPF, name);
            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

            assertEquals(CPF, createUserRequest.getCpf());
            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    class ValidateCPF {

        @Test
        void cpfNull() {
            CreateUserRequest createUserRequest = new CreateUserRequest(null, NAME);
            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

            assertEquals(NAME, createUserRequest.getName());
            assertFalse(violations.isEmpty());
        }

        @Test
        void cpfEmpty() {
            CreateUserRequest createUserRequest = new CreateUserRequest("", NAME);
            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

            assertEquals(NAME, createUserRequest.getName());
            assertFalse(violations.isEmpty());
        }

        @Test
        void cpfSizeMin() {
            CreateUserRequest createUserRequest = new CreateUserRequest("1".repeat(13), NAME);
            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

            assertEquals(NAME, createUserRequest.getName());
            assertFalse(violations.isEmpty());
        }

        @Test
        void cpfSizeMax() {
            CreateUserRequest createUserRequest = new CreateUserRequest("1".repeat(15), NAME);
            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

            assertEquals(NAME, createUserRequest.getName());
            assertFalse(violations.isEmpty());
        }

        @Test
        void cpfFormat() {
            CreateUserRequest createUserRequest = new CreateUserRequest("1".repeat(14), NAME);
            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

            assertEquals(NAME, createUserRequest.getName());
            assertFalse(violations.isEmpty());
        }

        @Test
        void cpfInvalid() {
            CreateUserRequest createUserRequest = new CreateUserRequest("123.456.789-00", NAME);
            Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(createUserRequest);

            assertEquals(NAME, createUserRequest.getName());
            assertFalse(violations.isEmpty());
        }

    }
}