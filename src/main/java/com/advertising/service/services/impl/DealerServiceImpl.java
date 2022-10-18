package com.advertising.service.services.impl;

import com.advertising.service.entities.DealerEntity;
import com.advertising.service.exceptions.ResourceIsAlreadyExistException;
import com.advertising.service.exceptions.ResourceNotFoundException;
import com.advertising.service.mappers.DealerMapper;
import com.advertising.service.models.Dealer;
import com.advertising.service.models.POJO.DealerPojo;
import com.advertising.service.repositories.DealerRepository;
import com.advertising.service.services.DealerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DealerServiceImpl implements DealerService {

    static final String DEALER_IDENTIFIER_NOT_FOUND_MESSAGE = "Dealer not found with the id: {0} ";
    static final String DEALER_ALREADY_EXIST_MESSAGE = "Dealer is already exist with the email: {0} ";

    final DealerRepository dealerRepository;
    final DealerMapper dealerMapper;

    @Override
    public DealerPojo createDealer(Dealer dealer) {

        /* Check if dealer is already exist */
        if (dealerRepository.existsByEmail(dealer.getEmail())){
            throw new ResourceIsAlreadyExistException(MessageFormat.format(DEALER_ALREADY_EXIST_MESSAGE, dealer.getEmail()), HttpStatus.CONFLICT);
        }
        /* Getting related Entity */
        DealerEntity dealerEntity = dealerMapper.asDealerEntity(dealer);

        /* Saving entity */
        DealerPojo createdDealer = dealerMapper.asDealerPojo(dealerRepository.save(dealerEntity));

        log.info("createDealer end ok - dealerId: {}", createdDealer.getId());
        log.trace("createDealer end ok - dealer: {}", createdDealer);

        return  createdDealer;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DealerPojo> readAllByFilters(List<Integer> dealerIds, String email, List<Integer> limitsOfPublishedListings, Pageable pageable) {

        /* Getting dealers */
        Page<DealerPojo> dealers = dealerRepository.readAllByFilters(dealerIds, email, limitsOfPublishedListings, pageable).map(dealerMapper::asDealerPojo);

        log.info("readListingsByFilters end ok - dealerIds: {} - email: {} - limitOfPublishedListings: {} ", dealerIds, email, limitsOfPublishedListings);
        log.trace("readListingsByFilters end ok - dealerIds: {} - email: {} - limitsOfPublishedListings: {} ", dealerIds, email, limitsOfPublishedListings);

        return  dealers;
    }

    @Override
    @Transactional(readOnly = true)
    public DealerPojo readDealer(Integer dealerId) {

        /* Getting dealer by dealerId */
        DealerPojo dealer = dealerMapper.asDealerPojo(dealerRepository.findById(dealerId).orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format(DEALER_IDENTIFIER_NOT_FOUND_MESSAGE, dealerId), HttpStatus.NOT_FOUND)));

        log.info("readDealer is ok - id {}:", dealerId);
        log.trace("readDealer is ok - dealer {}:", dealer);

        return dealer;
    }

    @Override
    public DealerPojo updateDealer(Dealer dealer) {

        /* Checking dealer existence */
        if(!dealerRepository.existsById(dealer.getId())){
            throw new ResourceNotFoundException(MessageFormat.format(DEALER_IDENTIFIER_NOT_FOUND_MESSAGE, dealer.getId()), HttpStatus.NOT_FOUND);
        }
         /* Getting related Entity */
        DealerEntity dealerEntity = dealerMapper.asDealerEntity(dealer);

        /* Saving entity */
        DealerPojo updatedDealer = dealerMapper.asDealerPojo(dealerRepository.save(dealerEntity));

        log.info("updateDealer end ok - id: {}", updatedDealer.getId());
        log.trace("updateDealer end ok - dealer: {}", updatedDealer);

        return updatedDealer;
    }

    @Override
    public DealerPojo deleteDealer(Integer dealerId) {

        /* Getting dealer */
        DealerPojo deletedDealer = readDealer(dealerId);

        /* Deleting entity */
        dealerRepository.deleteById(dealerId);

        log.info("deleteDealer end ok - id: {}", dealerId);
        log.trace("deleteDealer end ok - dealer: {}", deletedDealer);

        return deletedDealer;
    }
}
