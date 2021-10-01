package com.raytotti.convertcurrency.conversion.infrastructure;

import com.raytotti.convertcurrency.conversion.domain.Conversion;
import com.raytotti.convertcurrency.conversion.domain.IConversionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ConversionRepository implements IConversionRepository {

    @Override
    public BigDecimal getRate(Conversion conversion) {
        return BigDecimal.ONE;
    }
}
