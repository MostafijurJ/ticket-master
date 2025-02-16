package com.learn.ms.event.presenter.rest.api;

import com.learn.ms.event.common.utils.AppUtils;
import com.learn.ms.event.common.utils.ResponseUtils;
import com.learn.ms.event.core.domain.enums.ResponseMessage;
import com.learn.ms.event.core.domain.model.ApiResponse;
import com.learn.ms.event.core.domain.request.VenueRequest;
import com.learn.ms.event.core.domain.response.VenueResponse;
import com.learn.ms.event.core.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppUtils.BASE_URL + "/venues")
@Tag(name = "Venue", description = "Venue API CRUD operations")
@RequiredArgsConstructor
public class VenueResource extends BaseResource {
    private final VenueService venueService;

    @PostMapping("/create")
    @Operation(summary = "Create Venue", description = "Creates a new venue with the provided details.")
    public ApiResponse<VenueResponse> createVenue(@RequestBody @Valid VenueRequest venue) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), venueService.createVenue(venue));
    }


    @GetMapping
    @Operation(summary = "Get All Venues", description = "Retrieves a list of all venues.")
    public ApiResponse<Page<VenueResponse>> getAllVenues(@ParameterObject Pageable pageable) {
        Page<VenueResponse> venues = venueService.getAllVenues(pageable);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), venues);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get Venue by ID", description = "Retrieves the details of a venue by its ID.")
    public ApiResponse<VenueResponse> getVenueById(@PathVariable Long id) {
        VenueResponse venueResponse = venueService.getVenueById(id);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), venueResponse);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Venue", description = "Updates the details of an existing venue by its ID.")
    public ApiResponse<VenueResponse> updateVenue(@PathVariable Long id, @RequestBody VenueRequest venue) {
        VenueResponse venueResponse = venueService.updateVenue(id, venue);
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), venueResponse);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Venue", description = "Deletes a venue by its ID.")
    public ApiResponse<VenueResponse> deleteVenue(@PathVariable Long id) {
        return ResponseUtils.createSuccessResponseObject(getMessage(ResponseMessage.OPERATION_SUCCESSFUL), venueService.deleteVenue(id));
    }


}
