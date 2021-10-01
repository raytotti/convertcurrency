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
    @NotNull(message = "{User.name.NotNull}")
    private UUID userId;

    private Currency originCurrency;

    private BigDecimal originValue;

    private Currency destinationCurrency;

}
