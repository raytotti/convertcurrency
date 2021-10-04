package com.raytotti.convertcurrency.transaction;

import com.raytotti.convertcurrency.transaction.domain.Currency;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@Tag("integration")
@AutoConfigureMockMvc
public class FindByIdTransactionIT {

    @Autowired
    private MockMvc mock;

    @Test
    @Sql(scripts = "classpath:db/it/base/transaction/FindByIdTransaction.sql")
    void find() throws Exception {
        String urlTemplate = "/api/v1/transactions/";
        String id = "a47be715-a79c-4666-b40f-d33629879fb2";

        BigDecimal rate = BigDecimal.valueOf(6.318592);
        BigDecimal originValue = BigDecimal.TEN;
        BigDecimal destinationValue = originValue.multiply(rate);

        mock.perform(get(urlTemplate + id))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", equalTo(id)))
                .andExpect(jsonPath("$.userId", equalTo("c3437503-ccd7-4290-9465-102102a9d748")))
                .andExpect(jsonPath("$.originCurrency", equalTo(Currency.BRL.toString())))
                .andExpect(jsonPath("$.originValue").value(originValue))
                .andExpect(jsonPath("$.destinationCurrency", equalTo(Currency.EUR.toString())))
                .andExpect(jsonPath("$.destinationValue").value(destinationValue.floatValue()))
                .andExpect(jsonPath("$.conversionRate").value(rate))
                .andExpect(jsonPath("$.createdAt").exists());
    }

}
