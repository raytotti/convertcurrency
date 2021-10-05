package com.raytotti.convertcurrency.transaction.application;

import com.raytotti.convertcurrency.transaction.domain.Currency;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CreateTransactionRequest {

    @NotNull(message = "{Transaction.userId.NotNull}")
    private UUID userId;

    @NotNull(message = "{Transaction.originCurrency.NotNull}")
    private Currency originCurrency;

    @NotNull(message = "{Transaction.originValue.NotNull}")
    private BigDecimal originValue;

    @NotNull(message = "{Transaction.destinationCurrency.NotNull}")
    private Currency destinationCurrency;

}
