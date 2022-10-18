package com.advertising.service.services;

import com.advertising.service.models.Listing;
import com.advertising.service.models.ManageListing;
import com.advertising.service.models.POJO.ListingPojo;
import com.advertising.service.models.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ListingService {

    ListingPojo createListing(Listing listing);
    Page<ListingPojo> readAllByFilters(Integer dealerId, LocalDateTime createdAt, State state, List<Long> prices, String vehicle, Pageable pageable);
    ListingPojo readListing(Integer listingId);
    ListingPojo updateListing(Listing listing);
    ListingPojo deleteListing(Integer listingId);
    ListingPojo manageListing(Integer listingId, ManageListing manageListing);
}
