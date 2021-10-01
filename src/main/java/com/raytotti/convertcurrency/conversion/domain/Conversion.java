package com.raytotti.convertcurrency.conversion.domain;

import com.raytotti.convertcurrency.transaction.domain.Currency;
import lombok.Getter;

@Getter
public class Conversion {

    private final Currency to;
    private final Currency from;

    public Conversion(Currency to, Currency from) {
        this.to = to;
        this.from = from;
    }
}
