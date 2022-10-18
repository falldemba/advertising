package com.advertising.service.mappers;

import com.advertising.service.entities.DealerEntity;
import com.advertising.service.models.Dealer;
import com.advertising.service.models.POJO.DealerPojo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DealerMapper {

    DealerEntity asDealerEntity(Dealer dealer);

/*    @Mapping(source = "listings.id", target = "listings.id")
    @Mapping(source = "listings.vehicle", target = "listings.vehicle")
    @Mapping(source = "listings.price", target = "listings.price")
    @Mapping(source = "listings.state", target = "listings.state")
    @Mapping(source = "listings.createdAt", target = "listings.createdAt")*/
    DealerPojo asDealerPojo(DealerEntity dealerEntity);

}
