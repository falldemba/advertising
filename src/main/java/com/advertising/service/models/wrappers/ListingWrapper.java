package com.advertising.service.models.wrappers;

import com.advertising.service.models.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingWrapper {
    int id;
    String vehicle;
    Long price;
    State state;
    LocalDateTime createdAt;
}
