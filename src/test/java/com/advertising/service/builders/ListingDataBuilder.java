package com.advertising.service.builders;

import com.advertising.service.models.Listing;
import com.advertising.service.models.State;

import java.time.LocalDateTime;

public class ListingDataBuilder {
    public static Listing.ListingBuilder getBuilder(){
        return Listing.builder()
                .dealerId(2)
                .createdAt(LocalDateTime.now())
                .price(2000L)
                .vehicle("PEPPERONI")
                .state(State.draft);
    }
}
