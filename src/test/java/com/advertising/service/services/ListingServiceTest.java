package com.advertising.service.services;

import com.advertising.service.ServiceTest;
import com.advertising.service.builders.ListingDataBuilder;
import com.advertising.service.exceptions.MaximumListingCountReachedException;
import com.advertising.service.exceptions.RequestStateException;
import com.advertising.service.exceptions.ResourceNotFoundException;
import com.advertising.service.models.Listing;
import com.advertising.service.models.POJO.ListingPojo;
import com.advertising.service.models.State;
import com.advertising.service.repositories.ListingRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ActiveProfiles(profiles = {"test"})
public class ListingServiceTest extends ServiceTest {

    @Autowired
    ListingService listingService;

    @Autowired
    ListingRepository listingRepository;

    @Test
    void shouldCreateListing(){

        // Given
        Listing listing = ListingDataBuilder.getBuilder().build();

        // When
        ListingPojo createdListing = listingService.createListing(listing);

        // Then
        assertNotNull(listing);
        assertNotNull(createdListing);
        assertEquals(listing.getDealerId(), createdListing.getDealer().getId());
        assertEquals(listing.getVehicle(), createdListing.getVehicle());
        assertEquals(listing.getState(), createdListing.getState());

    }

    @Test
    void shouldUpdateListing(){

        // Given
        String newVehicle = "BMW";
        int id = 2;
        Listing listing = ListingDataBuilder.getBuilder().id(id).vehicle(newVehicle).build();

        // When
        ListingPojo dealerUpdate = listingService.updateListing(listing);

        // Then
        assertEquals(listing.getId(), dealerUpdate.getId());
        assertEquals(newVehicle, dealerUpdate.getVehicle());
    }

    @Test
    @Sql("/init-data.sql")
    void shouldDeleteListing(){

        // Given
        int listingId = 7;

        // When
        listingService.deleteListing(listingId);

        // Then
        assertFalse(listingRepository.existsById(listingId));
    }

    @Test
    void shouldThrowResourceNotFoundException(){

        // Given
        int listingId = 123;

        // When
        assertThrows(ResourceNotFoundException.class, () -> listingService.readListing(listingId));
    }

    @Test
    void shouldThrowMaximumListingCountReachedException(){

        // Given
        int listingId = 6;
        State state = State.published;

        // Then
        assertThrows(MaximumListingCountReachedException.class, () -> listingService.manageListing(listingId, state));
    }

    @Test
    void shouldThrowRequestStateException(){

        // Given
        int listingId = 6;
        State state = State.draft;

        // Then
        assertThrows(RequestStateException.class, () -> listingService.manageListing(listingId, state));
    }

    @Test
    void shouldManageListingOK(){

        // Given
        int listingId = 5;
        State state = State.published;

        // When
        ListingPojo listingManaged = listingService.manageListing(listingId, state);

        // Then
        assertEquals(listingId, listingManaged.getId());
        assertEquals(state, listingManaged.getState());
    }

    @Test
    void shouldReadListings(){

        // When
        Page<ListingPojo> listings = listingService.readAllByFilters(null, null, null, null, null, PageRequest.of(0, Integer.MAX_VALUE));

        // Then
        assertEquals(6, listings.getTotalElements());
    }

}
