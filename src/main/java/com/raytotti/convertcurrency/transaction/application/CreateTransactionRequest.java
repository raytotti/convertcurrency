package com.raytotti.convertcurrency.transaction.application;

import com.raytotti.convertcurrency.transaction.domain.Currency;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CreateTransactionRequest {

    @Valid
    @NotNull(message = "{Transaction.userId.NotNull}")
    private UUID userId;

    @Valid
    @NotNull(message = "{Transaction.originCurrency.NotNull}")
    private Currency originCurrency;

    @Valid
    @NotNull(message = "{Transaction.originValue.NotNull}")
    private BigDecimal originValue;

    @Valid
    @NotNull(message = "{Transaction.destinationCurrency.NotNull}")
    private Currency destinationCurrency;

}
