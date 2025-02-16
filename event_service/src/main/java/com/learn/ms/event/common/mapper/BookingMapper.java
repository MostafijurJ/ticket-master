package com.learn.ms.event.common.mapper;

import com.learn.ms.event.core.domain.request.BookingRequest;
import com.learn.ms.event.core.domain.response.BookingResponse;
import com.learn.ms.event.data.entity.BookingHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {


    BookingHistory mapRequestToEntity(final BookingRequest request);

    BookingResponse mapEntityToResponse(BookingHistory bookingHistory);
}
