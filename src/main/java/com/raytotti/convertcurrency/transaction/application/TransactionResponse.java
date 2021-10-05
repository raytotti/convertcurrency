package com.raytotti.convertcurrency.transaction.application;

import com.raytotti.convertcurrency.transaction.domain.Currency;
import com.raytotti.convertcurrency.transaction.domain.Transaction;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TransactionResponse {

    private UUID id;
    private UUID userId;
    private Currency originCurrency;
    private BigDecimal originValue;
    private Currency destinationCurrency;
    private BigDecimal destinationValue;
    private BigDecimal conversionRate;
    private ZonedDateTime createdAt;

    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(transaction.getId(),
                transaction.getUserId(),
                transaction.getOriginCurrency(),
                transaction.getOriginValue(),
                transaction.getDestinationCurrency(),
                transaction.getOriginValue().multiply(transaction.getConversionRate()),
                transaction.getConversionRate(),
                transaction.getCreatedAt());
    }

}
