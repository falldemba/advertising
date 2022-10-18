package com.advertising.service.models.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealerWrapper {
    int id;
    String name;
    int limitOfPublishedListings;
    String email;
}
