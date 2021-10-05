package com.raytotti.convertcurrency.conversion.domain;

import com.raytotti.convertcurrency.transaction.domain.Currency;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Conversion {

    private final Currency to;
    private final Currency from;

    public static Conversion of(Currency to, Currency from) {
        return new Conversion(to, from);
    }
}
