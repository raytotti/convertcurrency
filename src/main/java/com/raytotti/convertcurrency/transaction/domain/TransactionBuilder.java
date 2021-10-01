package com.raytotti.convertcurrency.transaction.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class TransactionBuilder {

    private UUID id;
    private UUID userId;
    private Currency originCurrency;
    private BigDecimal originValue;
    private Currency destinationCurrency;
    private BigDecimal conversionRate;
    private ZonedDateTime createdAt;

    public Transaction build() {
        id = UUID.randomUUID();
        createdAt = ZonedDateTime.now();
        return new Transaction(this);
    }

    public TransactionBuilder userId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public TransactionBuilder originCurrency(Currency currency) {
        this.originCurrency = currency;
        return this;
    }

    public TransactionBuilder originValue(BigDecimal value) {
        this.originValue = value;
        return this;
    }

    public TransactionBuilder destinationCurrency(Currency currency) {
        this.destinationCurrency = currency;
        return this;
    }

    public TransactionBuilder conversionRate(BigDecimal conversionRate) {
        this.conversionRate = conversionRate;
        return this;
    }
}
