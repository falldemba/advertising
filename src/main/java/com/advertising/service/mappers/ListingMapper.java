package com.advertising.service.mappers;

import com.advertising.service.entities.ListingEntity;
import com.advertising.service.models.Listing;
import com.advertising.service.models.POJO.ListingPojo;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ListingMapper {

    @Mapping(source = "dealerId", target = "dealer_id")
    ListingEntity asListingEntity(Listing listing);

    ListingPojo asListing(ListingEntity listingEntity);
}
