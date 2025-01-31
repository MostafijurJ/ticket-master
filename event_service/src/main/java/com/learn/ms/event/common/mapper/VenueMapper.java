package com.learn.ms.event.common.mapper;

import com.learn.ms.event.core.domain.request.VenueRequest;
import com.learn.ms.event.core.domain.response.VenueResponse;
import com.learn.ms.event.data.entity.Venue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    VenueResponse mapToResponse(final Venue entity);

    Venue mapRequestToEntity(final VenueRequest request);

}
