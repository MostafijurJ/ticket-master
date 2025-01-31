package com.learn.ms.event.core.service;

import com.learn.ms.event.common.mapper.VenueMapper;
import com.learn.ms.event.core.domain.enums.ResponseMessage;
import com.learn.ms.event.core.domain.exceptions.InvalidRequestDataException;
import com.learn.ms.event.core.domain.request.VenueRequest;
import com.learn.ms.event.core.domain.response.VenueResponse;
import com.learn.ms.event.data.entity.Venue;
import com.learn.ms.event.data.repository.VenueRepository;
import com.learn.ms.event.presenter.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl extends BaseService implements VenueService {
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    @Override
    public VenueResponse createVenue(VenueRequest venueRequest) {
        Venue venue = venueMapper.mapRequestToEntity(venueRequest);
        Venue savedVenue = venueRepository.save(venue);
        return venueMapper.mapToResponse(savedVenue);
    }

    @Override
    public VenueResponse updateVenue(Long id, VenueRequest venueRequest) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.VENUE_NOT_FOUND));

        updateVenueDetails(venue, venueRequest);

        Venue updatedVenue = venueRepository.save(venue);
        return venueMapper.mapToResponse(updatedVenue);
    }

    private void updateVenueDetails(Venue venue, VenueRequest venueRequest) {
        if (ObjectUtils.isNotEmpty(venueRequest.getName())) {
            venue.setName(venueRequest.getName());
        }
        if (ObjectUtils.isNotEmpty(venueRequest.getAddress())) {
            venue.setAddress(venueRequest.getAddress());
        }
        if (ObjectUtils.isNotEmpty(venueRequest.getLocation())) {
            venue.setLocation(venueRequest.getLocation());
        }
        if (ObjectUtils.isNotEmpty(venueRequest.getCity())) {
            venue.setCity(venueRequest.getCity());
        }
        if (ObjectUtils.isNotEmpty(venueRequest.getCountry())) {
            venue.setCountry(venueRequest.getCountry());
        }
        if (venueRequest.getLatitude() != null) {
            venue.setLatitude(venueRequest.getLatitude());
        }
        if (venueRequest.getLongitude() != null) {
            venue.setLongitude(venueRequest.getLongitude());
        }
    }

    @Override
    public Page<VenueResponse> getAllVenues(Pageable pageable) {
        return venueRepository.findAll(pageable).map(venueMapper::mapToResponse);
    }

    @Override
    public VenueResponse getVenueById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.VENUE_NOT_FOUND));
        return venueMapper.mapToResponse(venue);
    }

    @Override
    public VenueResponse deleteVenue(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.VENUE_NOT_FOUND));
        venueRepository.delete(venue);
        return venueMapper.mapToResponse(venue);
    }
}
