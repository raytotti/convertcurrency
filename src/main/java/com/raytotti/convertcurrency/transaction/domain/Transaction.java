package com.raytotti.convertcurrency.transaction.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "transaction")
public class Transaction {

    @Id
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private Currency originCurrency;

    private BigDecimal originValue;

    @Enumerated(EnumType.STRING)
    private Currency destinationCurrency;

    private BigDecimal conversionRate;

    private ZonedDateTime createdAt;

    Transaction(TransactionBuilder builder) {
        this.id = Objects.requireNonNull(builder.getId());
        this.userId = Objects.requireNonNull(builder.getUserId());
        this.originCurrency = Objects.requireNonNull(builder.getOriginCurrency());
        this.originValue = Objects.requireNonNull(builder.getOriginValue());
        this.destinationCurrency = Objects.requireNonNull(builder.getDestinationCurrency());
        this.conversionRate = Objects.requireNonNull(builder.getConversionRate());
        this.createdAt = Objects.requireNonNull(builder.getCreatedAt());
    }

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }


}
