package com.raytotti.convertcurrency.transaction.application;

import com.raytotti.convertcurrency.transaction.domain.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateTransactionRequestTest {

    private final UUID USER_ID = UUID.fromString("1e4382a2-0500-4dca-8e54-d554ba8073d4");
    private final Currency ORIGIN_CURRENCY = Currency.EUR;
    private final BigDecimal ORIGIN_VALUE = BigDecimal.TEN;
    private final Currency DESTINATION_CURRENCY = Currency.JPY;

    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void build() {
        CreateTransactionRequest transactionRequest = new CreateTransactionRequest(
                USER_ID,
                ORIGIN_CURRENCY,
                ORIGIN_VALUE,
                DESTINATION_CURRENCY);
        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(transactionRequest);

        assertEquals(USER_ID, transactionRequest.getUserId());
        assertEquals(ORIGIN_CURRENCY, transactionRequest.getOriginCurrency());
        assertEquals(ORIGIN_VALUE, transactionRequest.getOriginValue());
        assertEquals(DESTINATION_CURRENCY, transactionRequest.getDestinationCurrency());
        assertTrue(violations.isEmpty());
    }

    @Test
    void userIdNull() {
        CreateTransactionRequest transactionRequest = new CreateTransactionRequest(
                null,
                ORIGIN_CURRENCY,
                ORIGIN_VALUE,
                DESTINATION_CURRENCY);
        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(transactionRequest);

        assertEquals(ORIGIN_CURRENCY, transactionRequest.getOriginCurrency());
        assertEquals(ORIGIN_VALUE, transactionRequest.getOriginValue());
        assertEquals(DESTINATION_CURRENCY, transactionRequest.getDestinationCurrency());
        assertFalse(violations.isEmpty());
    }

    @Test
    void originCurrencyNull() {
        CreateTransactionRequest transactionRequest = new CreateTransactionRequest(
                USER_ID,
                null,
                ORIGIN_VALUE,
                DESTINATION_CURRENCY);
        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(transactionRequest);

        assertEquals(USER_ID, transactionRequest.getUserId());
        assertEquals(ORIGIN_VALUE, transactionRequest.getOriginValue());
        assertEquals(DESTINATION_CURRENCY, transactionRequest.getDestinationCurrency());
        assertFalse(violations.isEmpty());
    }

    @Test
    void originValueNull() {
        CreateTransactionRequest transactionRequest = new CreateTransactionRequest(
                USER_ID,
                ORIGIN_CURRENCY,
                null,
                DESTINATION_CURRENCY);
        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(transactionRequest);

        assertEquals(USER_ID, transactionRequest.getUserId());
        assertEquals(ORIGIN_CURRENCY, transactionRequest.getOriginCurrency());
        assertEquals(DESTINATION_CURRENCY, transactionRequest.getDestinationCurrency());
        assertFalse(violations.isEmpty());
    }

    @Test
    void destinationCurrencyNull() {
        CreateTransactionRequest transactionRequest = new CreateTransactionRequest(
                USER_ID,
                ORIGIN_CURRENCY,
                ORIGIN_VALUE,
                null);
        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(transactionRequest);

        assertEquals(USER_ID, transactionRequest.getUserId());
        assertEquals(ORIGIN_CURRENCY, transactionRequest.getOriginCurrency());
        assertEquals(ORIGIN_VALUE, transactionRequest.getOriginValue());
        assertFalse(violations.isEmpty());
    }

}