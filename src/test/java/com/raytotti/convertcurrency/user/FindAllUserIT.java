package com.raytotti.convertcurrency.user;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@Tag("integration")
@AutoConfigureMockMvc
@Sql(scripts = "classpath:db/it/base/user/FindAllUser.sql")
public class FindAllUserIT {

    @Autowired
    private MockMvc mock;

    private final String URL_TEMPLATE = "/api/v1/users";

    @Test
    void findPageDefault() throws Exception {
        mock.perform(get(URL_TEMPLATE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.items.length()", is(3)))
                .andExpect(jsonPath("$.hasNext", is(false)))
                .andExpect(jsonPath("$.items[0].id", equalTo("a47be715-a79c-4666-b40f-d33629879fb2")))
                .andExpect(jsonPath("$.items[0].cpf", equalTo("911.135.730-40")))
                .andExpect(jsonPath("$.items[0].name", equalTo("Maria")));
    }

    @Test
    void findPage() throws Exception {
        mock.perform(get(URL_TEMPLATE)
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.items.length()", is(2)))
                .andExpect(jsonPath("$.hasNext", is(true)))
                .andExpect(jsonPath("$.items[0].id", equalTo("a47be715-a79c-4666-b40f-d33629879fb2")))
                .andExpect(jsonPath("$.items[0].cpf", equalTo("911.135.730-40")))
                .andExpect(jsonPath("$.items[0].name", equalTo("Maria")));

        mock.perform(get(URL_TEMPLATE)
                        .param("page", "1")
                        .param("size", "2"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.items.length()", is(1)))
                .andExpect(jsonPath("$.hasNext", is(false)))
                .andExpect(jsonPath("$.items[0].id", equalTo("c3437503-ccd7-4290-9465-102102a9d748")))
                .andExpect(jsonPath("$.items[0].cpf", equalTo("430.609.538-05")))
                .andExpect(jsonPath("$.items[0].name", equalTo("Ray")));
    }
}
