package com.raytotti.convertcurrency.conversion.infrastructure;

import com.raytotti.convertcurrency.conversion.domain.Conversion;
import com.raytotti.convertcurrency.conversion.domain.IConversionService;
import com.raytotti.convertcurrency.conversion.exception.TaxRateNotFound;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

@Service
public class ConversionService implements IConversionService {

    @Value("${conversion.access-key}")
    private String ACCESS_KEY;

    @Value("${conversion.url}")
    private String URL;

    @Value("${conversion.uri}")
    private String URI;

    @Override
    public BigDecimal getRate(Conversion conversion) {
        Optional<ExchangeRatesResponse> exchangeRatesResponse = WebClient
                .create(URL)
                .get()
                .uri(URI, ACCESS_KEY, conversion.getTo(), conversion.getFrom())
                .retrieve()
                .bodyToMono(ExchangeRatesResponse.class).blockOptional();
        ExchangeRatesResponse rates = exchangeRatesResponse.orElseThrow(TaxRateNotFound::new);

        BigDecimal taxRateTo = rates.getTaxRate(conversion.getTo());
        if (Objects.isNull(taxRateTo)) throw new TaxRateNotFound();

        BigDecimal taxRateFrom = rates.getTaxRate(conversion.getFrom());
        if (Objects.isNull(taxRateFrom)) throw new TaxRateNotFound();

        return taxRateFrom.divide(taxRateTo, 6, RoundingMode.HALF_EVEN);
    }
}
