package com.advertising.service.models.POJO;

import com.advertising.service.models.State;
import com.advertising.service.models.wrappers.DealerWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingPojo {
    int id;
    DealerWrapper dealer;
    String vehicle;
    Long price;
    State state;
    LocalDateTime createdAt;
}
