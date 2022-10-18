package com.advertising.service.repositories;

import com.advertising.service.entities.ListingEntity;
import com.advertising.service.models.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.collections4.CollectionUtils;
import com.advertising.service.entities.QListingEntity;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public interface ListingRepository extends CrudRepository<ListingEntity, Integer>, QuerydslPredicateExecutor<ListingEntity> {
    List<ListingEntity> findAllByDealer_id(int dealerId);
    default Page<ListingEntity> readAllByFilters(
            Integer dealerId,
            LocalDateTime createdAt,
            State state,
            List<Long> prices,
            String vehicle,
            Pageable pageable){
        BooleanBuilder builder = new BooleanBuilder();
        if(dealerId != null){
            builder.and(QListingEntity.listingEntity.dealer_id.eq(dealerId));
        }
        if(createdAt != null){
            builder.and(QListingEntity.listingEntity.createdAt.loe(createdAt));
        }
        if(!Objects.isNull(state)){
            builder.and(QListingEntity.listingEntity.state.eq(state));
        }
        if(StringUtils.isNotEmpty(vehicle)){
            builder.and(QListingEntity.listingEntity.vehicle.eq(vehicle));
        }
        if(CollectionUtils.isNotEmpty(prices)){
            builder.and(QListingEntity.listingEntity.price.in(prices));
        }
        return findAll(builder, pageable);
    }
}
