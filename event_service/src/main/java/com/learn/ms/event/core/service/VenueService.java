package com.learn.ms.event.core.service;

import com.learn.ms.event.core.domain.request.VenueRequest;
import com.learn.ms.event.core.domain.response.VenueResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VenueService {
    VenueResponse createVenue(VenueRequest venue);
    Page<VenueResponse> getAllVenues(Pageable pageable);
    VenueResponse getVenueById(Long id);
    VenueResponse updateVenue(Long id, VenueRequest venue);
    VenueResponse deleteVenue(Long id);

}
