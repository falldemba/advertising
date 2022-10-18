package com.advertising.service.models.POJO;

import com.advertising.service.models.wrappers.ListingWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealerPojo {
    int id;
    String name;
    int limitOfPublishedListings;
    String email;
    List<ListingWrapper> listings;
}
