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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@Tag("integration")
@AutoConfigureMockMvc
public class FindByIdUserIT {

    @Autowired
    private MockMvc mock;

    @Test
    @Sql(scripts = "classpath:db/it/base/user/FindByIdUser.sql")
    void find() throws Exception {
        String userId = "c3437503-ccd7-4290-9465-102102a9d748";
        String cpf = "430.609.538-05";
        String name = "Ray";

        mock.perform(get("/api/v1/users/" + userId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.cpf", equalTo(cpf)));
    }
}
