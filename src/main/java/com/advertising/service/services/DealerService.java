package com.advertising.service.services;

import com.advertising.service.models.Dealer;
import com.advertising.service.models.POJO.DealerPojo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DealerService {
    DealerPojo createDealer(Dealer dealer);
    Page<DealerPojo> readAllByFilters(List<Integer> dealerIds, String email, List<Integer> limitsOfPublishedListings, Pageable pageable);
    DealerPojo readDealer(Integer dealerId);
    DealerPojo updateDealer(Dealer dealer);
    DealerPojo deleteDealer(Integer dealerId);
}
