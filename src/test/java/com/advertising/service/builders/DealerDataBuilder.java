package com.advertising.service.builders;

import com.advertising.service.models.Dealer;

public class DealerDataBuilder {
    public static Dealer.DealerBuilder getBuilder(){
        return Dealer.builder()
                .name("dealerName")
                .email("dealer@gmail.com")
                .limitOfPublishedListings(1);
    }
}
