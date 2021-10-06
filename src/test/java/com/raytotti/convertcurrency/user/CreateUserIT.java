package com.raytotti.convertcurrency.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raytotti.convertcurrency.user.application.CreateUserRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@Tag("integration")
@AutoConfigureMockMvc
public class CreateUserIT {

    @Autowired
    private MockMvc mock;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void create() throws Exception {
        String urlTemplate = "/api/v1/users";
        String cpf = "430.609.538-05";
        String name = "Ray Toti Felix de Araujo";
        CreateUserRequest userRequest = new CreateUserRequest(cpf, name);

        mock.perform(post(urlTemplate).contentType(APPLICATION_JSON).content(mapper.writeValueAsBytes(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(LOCATION))
                .andExpect(redirectedUrlPattern("**//**" + urlTemplate + "/*"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.cpf", equalTo(cpf)));
    }
}
