package com.advertising.service.controllers;

import com.advertising.service.ServiceTest;
import com.advertising.service.builders.DealerDataBuilder;
import com.advertising.service.models.Dealer;
import com.advertising.service.models.POJO.DealerPojo;
import com.advertising.service.repositories.DealerRepository;
import com.advertising.service.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"test"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureMockMvc
public class DealerControllerTest extends ServiceTest {
    public final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    TestUtils utils;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DealerRepository dealerRepository;

    @Test
    void shouldUpdateDealer() throws Exception {

        // Given
        int dealerId = 3;
        String name = "newName";
        String newEmail = "newEmail@gmail.com";
        Dealer dealer = DealerDataBuilder.getBuilder().email(newEmail).name(name).build();

        // When
        mockMvc.perform(put("/v1/dealers/" + dealerId)
                .contentType(APPLICATION_JSON_UTF8)
                .content(utils.convertObjectToJsonBytes(dealer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(newEmail));
    }

    @Test
    void shouldThrowExceptionUpdateNotExistingDealer() throws Exception {

        // Given
        Dealer dealer = DealerDataBuilder.getBuilder().build();
        int dealerId = 999;

        // When
        mockMvc.perform(put("/v1/dealers/" + dealerId)
                .contentType(APPLICATION_JSON_UTF8)
                .content(utils.convertObjectToJsonBytes(dealer)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateDealer() throws Exception {

        // Given
        Dealer createdDealer = DealerDataBuilder.getBuilder().build();

        // When
        mockMvc.perform(post("/v1/dealers")
                .contentType(utils.APPLICATION_JSON_UTF8)
                .content(utils.convertObjectToJsonBytes(createdDealer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(createdDealer.getName()));
    }

    @Test
    void shouldReadDealer() throws Exception {

        // Given
        int dealerId = 2;

        // When
        mockMvc.perform(get("/v1/dealers/" + dealerId)
                        .contentType(utils.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dealerId));
    }

    @Test
    void shouldThrowExceptionReadNotExistingDealer() throws Exception {

        // Given
        int dealerId = 999;

        // When
        mockMvc.perform(get("/v1/dealers/" + dealerId)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteDealer() throws Exception {

        // Given
        int dealerId = 7;

        // When
        mockMvc.perform(delete("/v1/dealers/" + dealerId)
                        .contentType(utils.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

        // Then
        assertFalse(dealerRepository.existsById(dealerId));
    }

    @Test
    void shouldThrowExceptionDeleteNotExistingDealer() throws Exception{

        // Given
        int dealerId = 234;

        // When
        mockMvc.perform(delete("/v1/dealers/" + dealerId)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql("/init-data.sql")
    void shouldGetFilteredAndPaginatedDealerList() throws Exception {

        // Given
        Dealer dealer = DealerDataBuilder.getBuilder()
                .id(2)
                .limitOfPublishedListings(10)
                .build();
        // When
        String response = mockMvc.perform(get("/v1/dealers?" + "dealerIds=" + dealer.getId() +
                "&limitsOfPublishedListings=" + dealer.getLimitOfPublishedListings() +
                "&page=0" +
                "&size=5")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Then
        List<DealerPojo> dealersResultList = utils.convertJsonToObjectList(new ObjectMapper().readTree(response).get("content").toString(), DealerPojo.class);
        assertEquals(1, dealersResultList.size());
    }

}
