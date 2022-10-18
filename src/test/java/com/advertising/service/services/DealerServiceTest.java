package com.advertising.service.services;

import com.advertising.service.ServiceTest;
import com.advertising.service.builders.DealerDataBuilder;
import com.advertising.service.exceptions.ResourceIsAlreadyExistException;
import com.advertising.service.exceptions.ResourceNotFoundException;
import com.advertising.service.models.Dealer;
import com.advertising.service.models.POJO.DealerPojo;
import com.advertising.service.repositories.DealerRepository;
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
public class DealerServiceTest extends ServiceTest {

    @Autowired
    DealerService dealerService;

    @Autowired
    DealerRepository dealerRepository;

    @Test
    void shouldCreateDealer(){
        // Given
        Dealer dealer = DealerDataBuilder.getBuilder().build();

        // When
        DealerPojo createdDealer = dealerService.createDealer(dealer);

        // Then
        assertNotNull(dealer);
        assertNotNull(createdDealer);
        assertEquals(dealer.getName(), createdDealer.getName());
        assertEquals(dealer.getLimitOfPublishedListings(), createdDealer.getLimitOfPublishedListings());

    }

    @Test
    @Sql("/init-data.sql")
    void shouldUpdateDealer(){

        // Given
        String newName = "newName";
        int id = 2;
        String newEmail = "newEmail@gmail.com";
        Dealer dealer = DealerDataBuilder.getBuilder().id(id).email(newEmail).name(newName).build();

        // When
        DealerPojo dealerUpdate = dealerService.updateDealer(dealer);

        // Then
        assertEquals(dealer.getId(), dealerUpdate.getId());
        assertEquals(newName, dealerUpdate.getName());
        assertEquals(newEmail, dealerUpdate.getEmail());
    }

    @Test
    void shouldDeleteDealer(){

        // Given
        int dealerId = 2;

        // When
        dealerService.deleteDealer(dealerId);

        // Then
        assertFalse(dealerRepository.existsById(dealerId));
    }

    @Test
    void shouldThrowResourceNotFoundException(){

        // Given
        int dealerId = 123;

        // When
        assertThrows(ResourceNotFoundException.class, () -> dealerService.readDealer(dealerId));
    }

    @Test
    void shouldThrowResourceIsAlreadyExistException(){

        // Given
        Dealer dealer = DealerDataBuilder.getBuilder().build();

        // When
        assertThrows(ResourceIsAlreadyExistException.class, () -> dealerService.createDealer(dealer));
    }

    @Test
    void shouldReadDealers(){

        // When
        Page<DealerPojo> dealers = dealerService.readAllByFilters(null, null, null ,PageRequest.of(0, Integer.MAX_VALUE));

        // Then
        assertEquals(6, dealers.getTotalElements());
    }
}
