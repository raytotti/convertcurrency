package com.raytotti.convertcurrency.conversion.infrastructure;

import com.raytotti.convertcurrency.transaction.domain.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class ExchangeRatesResponse {

    private Map<Currency, BigDecimal> rates = new HashMap<>();

    public BigDecimal getTaxRate(Currency currency) {
        return rates.get(currency);
    }

}
