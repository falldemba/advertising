package com.advertising.service.builders;

import com.advertising.service.models.ManageListing;
import com.advertising.service.models.State;

public class ManageListingDataBuilder {
    public static ManageListing.ManageListingBuilder getBuilder(){
        return ManageListing.builder()
                .showException(true)
                .state(State.draft);
    }
}
