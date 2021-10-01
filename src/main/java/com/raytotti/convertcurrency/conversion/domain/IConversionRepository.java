package com.raytotti.convertcurrency.conversion.domain;

import java.math.BigDecimal;

public interface IConversionRepository {
    BigDecimal getRate(Conversion conversion);
}
