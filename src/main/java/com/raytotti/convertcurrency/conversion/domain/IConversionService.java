package com.raytotti.convertcurrency.conversion.domain;

import java.math.BigDecimal;

public interface IConversionService {
    BigDecimal getRate(Conversion conversion);
}
