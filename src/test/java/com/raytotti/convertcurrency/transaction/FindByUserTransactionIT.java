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
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@Tag("integration")
@AutoConfigureMockMvc
@Sql(scripts = "classpath:db/it/base/transaction/FindByUserTransaction.sql")
public class FindByUserTransactionIT {

    @Autowired
    private MockMvc mock;

    private final String URL_TEMPLATE = "/api/v1/transactions/users";
    private final String USER_ID = "c3437503-ccd7-4290-9465-102102a9d748";

    @Test
    void findPageDefault() throws Exception {
        BigDecimal rate = BigDecimal.valueOf(0.049119);
        BigDecimal originValue = BigDecimal.valueOf(40);
        BigDecimal destinationValue = originValue.multiply(rate);

        mock.perform(get(URL_TEMPLATE + "/" + USER_ID))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.items.length()", is(3)))
                .andExpect(jsonPath("$.hasNext", is(false)))
                .andExpect(jsonPath("$.items[0].id", equalTo("acbf7eab-a507-4cc2-a017-2b200bf64c3f")))
                .andExpect(jsonPath("$.items[0].userId", equalTo(USER_ID)))
                .andExpect(jsonPath("$.items[0].originCurrency", equalTo(Currency.BRL.toString())))
                .andExpect(jsonPath("$.items[0].originValue").value(originValue))
                .andExpect(jsonPath("$.items[0].destinationCurrency", equalTo(Currency.JPY.toString())))
                .andExpect(jsonPath("$.items[0].destinationValue").value(destinationValue.floatValue()))
                .andExpect(jsonPath("$.items[0].conversionRate").value(rate))
                .andExpect(jsonPath("$.items[0].createdAt").exists());
    }

    @Test
    void findPage() throws Exception {
        BigDecimal rate = BigDecimal.valueOf(0.049119);
        BigDecimal originValue = BigDecimal.valueOf(40);
        BigDecimal destinationValue = originValue.multiply(rate);

        mock.perform(get(URL_TEMPLATE + "/" + USER_ID)
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.items.length()", is(2)))
                .andExpect(jsonPath("$.hasNext", is(true)))
                .andExpect(jsonPath("$.items[0].id", equalTo("acbf7eab-a507-4cc2-a017-2b200bf64c3f")))
                .andExpect(jsonPath("$.items[0].userId", equalTo(USER_ID)))
                .andExpect(jsonPath("$.items[0].originCurrency", equalTo(Currency.BRL.toString())))
                .andExpect(jsonPath("$.items[0].originValue").value(originValue))
                .andExpect(jsonPath("$.items[0].destinationCurrency", equalTo(Currency.JPY.toString())))
                .andExpect(jsonPath("$.items[0].destinationValue").value(destinationValue.floatValue()))
                .andExpect(jsonPath("$.items[0].conversionRate").value(rate))
                .andExpect(jsonPath("$.items[0].createdAt").exists());

        rate = BigDecimal.valueOf(6.318592);
        originValue = BigDecimal.TEN;
        destinationValue = originValue.multiply(rate);

        mock.perform(get(URL_TEMPLATE + "/" + USER_ID)
                        .param("page", "1")
                        .param("size", "2"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.items.length()", is(1)))
                .andExpect(jsonPath("$.hasNext", is(false)))
                .andExpect(jsonPath("$.items[0].id", equalTo("a47be715-a79c-4666-b40f-d33629879fb2")))
                .andExpect(jsonPath("$.items[0].userId", equalTo(USER_ID)))
                .andExpect(jsonPath("$.items[0].originCurrency", equalTo(Currency.BRL.toString())))
                .andExpect(jsonPath("$.items[0].originValue").value(originValue))
                .andExpect(jsonPath("$.items[0].destinationCurrency", equalTo(Currency.EUR.toString())))
                .andExpect(jsonPath("$.items[0].destinationValue").value(destinationValue.floatValue()))
                .andExpect(jsonPath("$.items[0].conversionRate").value(rate))
                .andExpect(jsonPath("$.items[0].createdAt").exists());

    }
}
