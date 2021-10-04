package com.raytotti.convertcurrency.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raytotti.convertcurrency.conversion.domain.IConversionService;
import com.raytotti.convertcurrency.transaction.application.CreateTransactionRequest;
import com.raytotti.convertcurrency.transaction.domain.Currency;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@Tag("integration")
@AutoConfigureMockMvc
@Sql(scripts = "classpath:db/it/base/transaction/CreateTransaction.sql")
public class CreateTransactionIT {

    @Autowired
    private MockMvc mock;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private IConversionService conversionService;

    @Test
    void create() throws Exception {

        BigDecimal rate = BigDecimal.valueOf(6.318592);
        when(conversionService.getRate(any())).thenReturn(rate);

        UUID userId = UUID.fromString("c3437503-ccd7-4290-9465-102102a9d748");
        Currency originCurrency = Currency.BRL;
        BigDecimal originValue = BigDecimal.TEN;
        Currency destinationCurrency = Currency.EUR;

        BigDecimal destinationValue = BigDecimal.TEN.multiply(rate);

        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest(
                userId,
                originCurrency,
                originValue,
                destinationCurrency);

        String urlTemplate = "/api/v1/transactions";
        mock.perform(post(urlTemplate).contentType(APPLICATION_JSON).content(mapper.writeValueAsBytes(createTransactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(LOCATION))
                .andExpect(redirectedUrlPattern("**//**" + urlTemplate + "/*"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId", equalTo(userId.toString())))
                .andExpect(jsonPath("$.originCurrency", equalTo(originCurrency.toString())))
                .andExpect(jsonPath("$.originValue").value(originValue))
                .andExpect(jsonPath("$.destinationCurrency", equalTo(destinationCurrency.toString())))
                .andExpect(jsonPath("$.destinationValue").value(destinationValue.floatValue()))
                .andExpect(jsonPath("$.conversionRate").value(rate))
                .andExpect(jsonPath("$.createdAt").exists());
    }
}
