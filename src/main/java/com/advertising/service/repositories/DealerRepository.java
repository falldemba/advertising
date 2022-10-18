package com.advertising.service.repositories;

import com.advertising.service.entities.DealerEntity;
import com.advertising.service.entities.QDealerEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DealerRepository extends CrudRepository<DealerEntity, Integer>, QuerydslPredicateExecutor<DealerEntity> {
    default Page<DealerEntity> readAllByFilters(
            List<Integer> dealerIds,
            String email,
            List<Integer> limitsOfPublishedListings,
            Pageable pageable){
        BooleanBuilder builder = new BooleanBuilder();
        if(CollectionUtils.isNotEmpty(dealerIds)){
            builder.and(QDealerEntity.dealerEntity.id.in(dealerIds));
        }
        if(StringUtils.isNotEmpty(email)){
            builder.and(QDealerEntity.dealerEntity.email.eq(email));
        }
        if(CollectionUtils.isNotEmpty(limitsOfPublishedListings)){
            builder.and(QDealerEntity.dealerEntity.limitOfPublishedListings.in(limitsOfPublishedListings));
        }
        return findAll(builder, pageable);
    }

    boolean existsByEmail(String email);
}

