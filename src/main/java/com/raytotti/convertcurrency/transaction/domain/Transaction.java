package com.raytotti.convertcurrency.transaction.domain;

import com.raytotti.convertcurrency.transaction.application.CreateTransactionRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private UUID userId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency originCurrency;

    @NotNull
    private BigDecimal originValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency destinationCurrency;

    @NotNull
    private BigDecimal conversionRate;

    @NotNull
    private ZonedDateTime createdAt;

    public static Transaction of(CreateTransactionRequest request, BigDecimal rate) {
        return new Transaction(UUID.randomUUID(),
                Objects.requireNonNull(request.getUserId()),
                Objects.requireNonNull(request.getOriginCurrency()),
                Objects.requireNonNull(request.getOriginValue()),
                Objects.requireNonNull(request.getDestinationCurrency()),
                Objects.requireNonNull(rate),
                ZonedDateTime.now()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
