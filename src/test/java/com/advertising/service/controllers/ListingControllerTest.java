package com.advertising.service.controllers;

import com.advertising.service.ServiceTest;
import com.advertising.service.builders.ListingDataBuilder;
import com.advertising.service.models.Listing;
import com.advertising.service.models.POJO.ListingPojo;
import com.advertising.service.models.State;
import com.advertising.service.repositories.ListingRepository;
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
public class ListingControllerTest extends ServiceTest {
    public final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    TestUtils utils;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ListingRepository listingRepository;

    @Test
    void shouldUpdateListing() throws Exception {

        // Given
        int listingId = 3;
        String vehicle = "newVehicle";
        Listing listing = ListingDataBuilder.getBuilder().vehicle(vehicle).build();

        // When
        mockMvc.perform(put("/v1/listings/" + listingId)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(utils.convertObjectToJsonBytes(listing)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicle").value(vehicle));
    }

    @Test
    void shouldThrowExceptionUpdateNotExistingListing() throws Exception {

        // Given
        Listing listing = ListingDataBuilder.getBuilder().build();
        int listingId = 999;

        // When
        mockMvc.perform(put("/v1/listings/" + listingId)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(utils.convertObjectToJsonBytes(listing)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateListing() throws Exception {

        // Given
        Listing createdListing = ListingDataBuilder.getBuilder().build();

        // When
        mockMvc.perform(post("/v1/listings")
                        .contentType(utils.APPLICATION_JSON_UTF8)
                        .content(utils.convertObjectToJsonBytes(createdListing)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vehicle").value(createdListing.getVehicle()));
    }

    @Test
    void shouldReadListing() throws Exception {

        // Given
        int listingId = 2;

        // When
        mockMvc.perform(get("/v1/listings/" + listingId)
                        .contentType(utils.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(listingId));
    }

    @Test
    void shouldThrowExceptionReadNotExistingListing() throws Exception {

        // Given
        int listingId = 999;

        // When
        mockMvc.perform(get("/v1/listings/" + listingId)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql("/init-data.sql")
    void shouldDeleteListing() throws Exception {

        // Given
        int listingId = 7;

        // When
        mockMvc.perform(delete("/v1/listings/" + listingId)
                        .contentType(utils.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

        // Then
        assertFalse(listingRepository.existsById(listingId));
    }

    @Test
    void shouldThrowExceptionDeleteNotExistingListing() throws Exception{

        // Given
        int listingId = 234;

        // When
        mockMvc.perform(delete("/v1/listings/" + listingId)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldManageListing() throws Exception {

        // Given
        int listingId = 5;
        State state = State.published;

        // When
        mockMvc.perform(put("/v1/listings/manage/" + listingId)
                        .contentType(APPLICATION_JSON_UTF8)
                .content(utils.convertObjectToJsonBytes(state)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(listingId))
                .andExpect(jsonPath("$.state").value(state.name()));
    }

    @Test
    void shouldGetFilteredAndPaginatedListingList() throws Exception {

        // Given
        Listing listing = ListingDataBuilder.getBuilder()
                .dealerId(2)
                .price(1023L)
                .state(State.draft)
                .vehicle("DODGE")
                .build();
        // When
        String response = mockMvc.perform(get("/v1/listings?" +
                        "dealerId=" + listing.getDealerId() +
                        "&state=" + listing.getState() +
                        "&prices=" + listing.getPrice() +
                        "&vehicle=" + listing.getVehicle() +
                        "&page=0" +
                        "&size=5")
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Then
        List<ListingPojo> listingsResultList = utils.convertJsonToObjectList(new ObjectMapper().readTree(response).get("content").toString(), ListingPojo.class);
        assertEquals(1, listingsResultList.size());
    }

}
