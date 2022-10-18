package com.advertising.service.controllers;

import com.advertising.service.models.Listing;
import com.advertising.service.models.POJO.ListingPojo;
import com.advertising.service.models.State;
import com.advertising.service.services.ListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@Tag(name = "Listing Controller", description = "Set of API to manage listings")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/v1/listings")
public class ListingController {

    final ListingService listingService;

    @Operation(summary = "Create listing", description = "This endpoint is used to create listing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ListingPojo createListing(@RequestBody final Listing listing){return listingService.createListing(listing);}

    @Operation(summary = "Get listing by identifier", description = "This endpoint is used to get listing by this identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "Resource access does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
    })
    @GetMapping("/{listingId}")
    @ResponseStatus(HttpStatus.OK)
    public ListingPojo readListing(@Parameter(description = "listing identifier", required = true) @PathVariable("listingId") int listingId){return listingService.readListing(listingId);}

    @Operation(summary = "Delete listing by identifier", description = "This endpoint is used to delete listing by this identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "Resource access does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
    })
    @DeleteMapping("/{listingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteListing(@Parameter(description = "the listing identifier to be deleted", required = true) @PathVariable("listingId") int listingId){ listingService.deleteListing(listingId);}

    @Operation(summary = "This endpoint is used to retrieve a paged list of listings filtered by several parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
    })
    @PageableAsQueryParam
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ListingPojo> readListingsByFilters(
            @Parameter(description = "dealerId of listing") @RequestParam(value = "dealerId", required = false) Integer dealerId,
            @Parameter(description = "Listing created at createdAt") @RequestParam(value = "createdAt", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime createdAt,
            @Parameter(description = "state of listing") @RequestParam(value = "state", required = false) State state,
            @Parameter(description = "prices of listing") @RequestParam(value = "prices", required = false) List<Long> prices,
            @Parameter(description = "vehicle of listing") @RequestParam(value = "vehicle", required = false) String vehicle,
            @Parameter(hidden = true) Pageable pageable
            )
    {return listingService.readAllByFilters(dealerId, createdAt, state, prices, vehicle, pageable);}

    @Operation(
            summary = "This endpoint is used to update Listing",
            description = "To update the listing, provide its ID and the body of the new listing"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "Resource access does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
    })
    @PutMapping("/{listingId}")
    @ResponseStatus(HttpStatus.OK)
    public ListingPojo updateListing(@Parameter(description = "the listing identifier to be updated", required = true) @PathVariable("listingId") int listingId,
                               @Valid @Parameter(description = "the listing body to be updated", required = true) @RequestBody Listing listing){
        listing.setId(listingId);
        return listingService.updateListing(listing);
    }

    @Operation(
            summary = "This endpoint is used to manage listing published/state",
            description = "To manage the listing, provide its ID and the body of the new state"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "Resource access does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
    })
    @PutMapping("/manage/{listingId}")
    @ResponseStatus(HttpStatus.OK)
    public ListingPojo manageListing(@Parameter(description = "the listing identifier to be managed", required = true) @PathVariable("listingId") int listingId,
                               @Valid @Parameter(description = "the state body to be managed", required = true) @RequestBody State state){
        return listingService.manageListing(listingId, state);
    }
}
