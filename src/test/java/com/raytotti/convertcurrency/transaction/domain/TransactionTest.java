package com.raytotti.convertcurrency.transaction.domain;

import com.raytotti.convertcurrency.transaction.application.CreateTransactionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private CreateTransactionRequest request;

    @BeforeEach
    void beforeEach() {
        request = new CreateTransactionRequest(
                UUID.randomUUID(),
                Currency.BRL,
                BigDecimal.TEN,
                Currency.USD);
    }

    @Test
    void build(){
        Transaction transaction = Transaction.of(request, BigDecimal.ONE);

        assertNotNull(transaction.getId());
        assertNotNull(transaction.getCreatedAt());
        assertEquals(BigDecimal.ONE, transaction.getConversionRate());
        assertEquals(request.getUserId(), transaction.getUserId());
        assertEquals(request.getOriginCurrency(), transaction.getOriginCurrency());
        assertEquals(request.getOriginValue(), transaction.getOriginValue());
        assertEquals(request.getDestinationCurrency(), transaction.getDestinationCurrency());
    }

    @Test
    void nullRequest() {
        request = null;
        assertThrows(NullPointerException.class, () -> Transaction.of(request, BigDecimal.ONE));
    }

    @Test
    void nullRate() {
        assertThrows(NullPointerException.class, () -> Transaction.of(request, null));
    }

}