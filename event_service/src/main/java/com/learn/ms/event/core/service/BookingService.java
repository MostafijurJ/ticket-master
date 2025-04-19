package com.learn.ms.event.core.service;

import com.learn.ms.event.core.domain.event.PostProcessingEvent;
import com.learn.ms.event.core.domain.request.BookingRequest;
import com.learn.ms.event.core.domain.response.BookingResponse;

public interface BookingService {

    BookingResponse bookTicket(BookingRequest request);

    void processBooking(PostProcessingEvent postProcessingEvent);
}
