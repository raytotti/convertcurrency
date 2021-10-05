package com.raytotti.convertcurrency.conversion.infrastructure;

import com.raytotti.convertcurrency.conversion.domain.Conversion;
import com.raytotti.convertcurrency.conversion.domain.IConversionService;
import com.raytotti.convertcurrency.conversion.exception.TaxRateNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

@Slf4j
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
        log.info("ConversionService -> getRate: Consulta de taxa de conversão em serviço externo, conversão utilizada {} no endereço {}", conversion, URL + URI);

        Optional<ExchangeRatesResponse> exchangeRatesResponse = WebClient
                .create(URL)
                .get()
                .uri(URI, ACCESS_KEY, conversion.getTo(), conversion.getFrom())
                .retrieve()
                .bodyToMono(ExchangeRatesResponse.class).blockOptional();
        log.info("ConversionService -> getRate: Retorno da Api Exchange: {}", exchangeRatesResponse);

        ExchangeRatesResponse rates = exchangeRatesResponse.orElseThrow(() -> {
            log.error("ConversionService -> getRate: Não foi possível encontrar as taxas de conversão.");
            throw new TaxRateNotFound();
        });

        BigDecimal taxRateTo = rates.getTaxRate(conversion.getTo());
        if (Objects.isNull(taxRateTo)) {
            log.error("ConversionService -> getRate: Taxa de conversão para {} não encontrada.", conversion.getTo());
            throw new TaxRateNotFound();
        }

        BigDecimal taxRateFrom = rates.getTaxRate(conversion.getFrom());
        if (Objects.isNull(taxRateFrom)) {
            log.error("ConversionService -> getRate: Taxa de conversão para {} não encontrada.", conversion.getFrom());
            throw new TaxRateNotFound();
        }

        log.info("ConversionService -> getRate: Taxas utilizadas para encontrar a conversão: {} e {}", taxRateTo, taxRateFrom);
        return taxRateFrom.divide(taxRateTo, 6, RoundingMode.HALF_EVEN);
    }
}
