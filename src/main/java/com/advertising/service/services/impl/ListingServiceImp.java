package com.advertising.service.services.impl;

import com.advertising.service.entities.DealerEntity;
import com.advertising.service.entities.ListingEntity;
import com.advertising.service.exceptions.MaximumListingCountReachedException;
import com.advertising.service.exceptions.RequestStateException;
import com.advertising.service.exceptions.ResourceNotFoundException;
import com.advertising.service.mappers.DealerMapper;
import com.advertising.service.mappers.ListingMapper;
import com.advertising.service.models.Listing;
import com.advertising.service.models.ManageListing;
import com.advertising.service.models.POJO.ListingPojo;
import com.advertising.service.models.State;
import com.advertising.service.repositories.DealerRepository;
import com.advertising.service.repositories.ListingRepository;
import com.advertising.service.services.ListingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ListingServiceImp implements ListingService {

    static final String LISTING_IDENTIFIER_NOT_FOUND_MESSAGE = "Listing not found with the id: {0}";
    static final String LISTING_STATE_MANAGEMENT_MESSAGE = "The listing is already at the requested state: {0}";
    static final String LISTING_PUBLISHED_REACHED_MESSAGE = "You have reached the number of listing allowed: {0}";

    final ListingRepository listingRepository;
    final ListingMapper listingMapper;
    final DealerMapper dealerMapper;
    final DealerRepository dealerRepository;

    @Override
    public ListingPojo createListing(Listing listing) {

        /* Getting related Entity */
        ListingEntity listingEntity = listingMapper.asListingEntity(listing);
        fillListingEntityDealerId(listing, listingEntity);

        /* Set state to default value if state is different to draft */
        if(!listingEntity.getState().equals(State.draft)){listingEntity.setState(State.draft);}

        /* Saving entity */
        ListingPojo createdListing = listingMapper.asListing(listingRepository.save(listingEntity));

        log.info("createListing end ok - listingId: {}", createdListing.getId());
        log.trace("createListing end ok - listing: {}", createdListing);

        return createdListing;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ListingPojo> readAllByFilters(Integer dealerId, LocalDateTime createdAt, State state, List<Long> prices, String vehicle, Pageable pageable) {

        /* Getting Listings */
        Page<ListingPojo> listings = listingRepository.readAllByFilters(dealerId, createdAt, state, prices, vehicle, pageable).map(listingMapper::asListing);

        log.info("readListingsByFilters end ok - dealerId: {} - createdAt: {} - state: {}", dealerId, createdAt, state);
        log.trace("readListingsByFilters end ok - dealerId: {} - createdAt: {} - state: {} - prices: {} - vehicles: {}", dealerId, createdAt, state, prices, vehicle);

        return  listings;
    }

    @Override
    @Transactional(readOnly = true)
    public ListingPojo readListing(Integer listingId) {

        /* Getting listing by listingId */
        ListingPojo listing = listingMapper.asListing(listingRepository.findById(listingId).orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format(LISTING_IDENTIFIER_NOT_FOUND_MESSAGE, listingId), HttpStatus.NOT_FOUND)));

        log.info("readListing is ok - id {}:", listingId);
        log.trace("readListing is ok - listing {}:", listing);

        return listing;
    }

    @Override
    public ListingPojo updateListing(Listing listing) {

        /* Checking listing existence */
        if (!listingRepository.existsById(listing.getId())) {
            throw new ResourceNotFoundException(MessageFormat.format(LISTING_IDENTIFIER_NOT_FOUND_MESSAGE, listing.getId()), HttpStatus.NOT_FOUND);
        }
        /* Getting related Entity */
        ListingEntity listingEntity = listingMapper.asListingEntity(listing);
        fillListingEntityDealerId(listing, listingEntity);

        /* Saving entity */
        ListingPojo updatedListing = listingMapper.asListing(listingRepository.save(listingEntity));

        log.info("updateListing end ok - id: {}", updatedListing.getId());
        log.trace("updateListing end ok - listing: {}", updatedListing);

        return updatedListing;
    }
    private void fillListingEntityDealerId(Listing listing, ListingEntity listingEntity){
        Optional<DealerEntity> dealerEntity = dealerRepository.findById(listing.getDealerId());
        dealerEntity.ifPresent(listingEntity::setDealer);
    }

    @Override
    public ListingPojo deleteListing(Integer listingId) {

        /* Getting listing */
        ListingPojo deletedListing = readListing(listingId);

        /* Deleting entity */
        listingRepository.deleteById(listingId);

        log.info("deleteDealer end ok - id: {}", listingId);
        log.trace("deleteDealer end ok - listing: {}", deletedListing);

        return deletedListing;
    }

    @Override
    public ListingPojo manageListing(Integer listingId, ManageListing manageListing) {

        log.info("start managing listing with the id: {}", listingId );
        /* Getting listing */
        Optional<ListingEntity> listingEntityToManaged = listingRepository.findById(listingId);

        /* Check if resource is present */
        if(!listingEntityToManaged.isPresent()){ throw new ResourceNotFoundException(MessageFormat.format(LISTING_IDENTIFIER_NOT_FOUND_MESSAGE, listingId), HttpStatus.NOT_FOUND); }

        /* Check if the state is not the same */
        if(listingEntityToManaged.get().getState().equals(manageListing.getState())){throw new RequestStateException(MessageFormat.format(LISTING_STATE_MANAGEMENT_MESSAGE, listingEntityToManaged.get().getId()), HttpStatus.BAD_REQUEST);}

        /* Getting listings by dealerId */
        List<ListingEntity> listingEntities = listingRepository.findAllByDealer_id(listingEntityToManaged.get().getDealer_id());

        /* fFnd the listings whose state is published */
        listingEntities = listingEntities.stream().filter(listingEntity -> listingEntity.getState().equals(State.published)).collect(Collectors.toList());

        listingEntities.stream().sorted(Comparator.comparing(ListingEntity::getPublishedAt).reversed());

        /* Getting maximum listing count */
        int numberOfPublished = listingEntities.size();

        /* Check if the maximum is not reached */
        if( numberOfPublished >= listingEntityToManaged.get().getDealer().getLimitOfPublishedListings()){
            if(manageListing.isShowException()){
                log.trace(MessageFormat.format(LISTING_PUBLISHED_REACHED_MESSAGE, numberOfPublished));
                throw new MaximumListingCountReachedException(MessageFormat.format(LISTING_PUBLISHED_REACHED_MESSAGE, numberOfPublished), HttpStatus.UNAUTHORIZED);
            }
            /* Get the oldest listing and unpublished the listing */
            ListingEntity oldestListingUnPublished = changeState(listingEntities.get(0), State.draft);

            log.info("UnPublishedOldestListing end ok - id: {}", oldestListingUnPublished.getId());
            log.trace("UnPublishedOldestListing end ok - listing: {}", oldestListingUnPublished);

            /* Published the listing */
            return  publishedListing(listingEntityToManaged.get(), manageListing.getState());
        }

        return publishedListing(listingEntityToManaged.get(), manageListing.getState());
    }

    private ListingEntity changeState(ListingEntity listingToBeUpdated, State state){
        /* Changing state */
        listingToBeUpdated.setState(state);
        return listingRepository.save(listingToBeUpdated);
    }

    private ListingPojo publishedListing(ListingEntity listingToBeUpdated, State state){

        listingToBeUpdated.setPublishedAt(LocalDateTime.now());
        ListingEntity listingPublished = changeState(listingToBeUpdated, state);

        log.info("Published the listing end ok - id: {}", listingPublished.getId());
        log.trace("Published the listing end ok - listing: {}", listingPublished);
        log.trace("Published the listing end ok - getPublishedAt: {}", listingPublished.getPublishedAt());
        return listingMapper.asListing(listingPublished);
    }
}
