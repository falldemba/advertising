package com.advertising.service.controllers;

import com.advertising.service.models.Dealer;
import com.advertising.service.models.POJO.DealerPojo;
import com.advertising.service.services.DealerService;
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
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@Tag(name = "Dealer Controller", description = "Set of API to manage dealers")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/v1/dealers")
public class DealerController {

    final DealerService dealerService;
    @Operation(summary = "Create Dealer", description = "This endpoint is used to create dealer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DealerPojo createDealer(@RequestBody final Dealer dealer){return dealerService.createDealer(dealer);}

    @Operation(summary = "Get dealer by identifier", description = "This endpoint is used to get dealer by this identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "Resource access does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
    })
    @GetMapping("/{dealerId}")
    @ResponseStatus(HttpStatus.OK)
    public DealerPojo readDealer(@Parameter(description = "Dealer identifier", required = true) @PathVariable("dealerId") int dealerId){return dealerService.readDealer(dealerId);}

    @Operation(summary = "Delete dealer by identifier", description = "This endpoint is used to delete dealer by this identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "Resource access does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
    })
    @DeleteMapping("/{dealerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDealer(@Parameter(description = "the dealer identifier to be deleted", required = true) @PathVariable("dealerId") int dealerId){ dealerService.deleteDealer(dealerId);}


    @Operation(summary = "This endpoint is used to retrieve a paged list of dealers filtered by several parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
    })
    @PageableAsQueryParam
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<DealerPojo> readAllByFilters(
            @Parameter(description = "dealerId of dealer") @RequestParam(value = "dealerIds", required = false) List<Integer> dealerIds,
            @Parameter(description = "email of dealer") @RequestParam(value = "email", required = false) String email,
            @Parameter(description = "limitsOfPublishedListings of dealer") @RequestParam(value = "limitsOfPublishedListings", required = false) List<Integer> limitsOfPublishedListings,
            @Parameter(hidden = true) Pageable pageable
    ){return dealerService.readAllByFilters(dealerIds, email, limitsOfPublishedListings, pageable);}

    @Operation(
            summary = "This endpoint is used to update Dealer",
            description = "To update the dealer, provide its ID and the body of the new dealer"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Request sent by the client was syntactically incorrect"),
            @ApiResponse(responseCode = "404", description = "Resource access does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")
    })
    @PutMapping("/{dealerId}")
    @ResponseStatus(HttpStatus.OK)
    public DealerPojo updateDealer(@Parameter(description = "the dealer identifier to be updated", required = true) @PathVariable("dealerId") int dealerId,
           @Valid @Parameter(description = "the dealer body to be updated", required = true) @RequestBody Dealer dealer){
       dealer.setId(dealerId);
       return dealerService.updateDealer(dealer);
    }
}
