package com.learn.ms.event.core.service;

import com.learn.ms.event.core.domain.event.TicketGenerationEvent;
import com.learn.ms.event.core.domain.request.EventRequest;
import com.learn.ms.event.core.domain.response.EventResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {
    EventResponse createEvent(EventRequest eventRequest);

    EventResponse getEventById(Long id);

    Page<EventResponse> getAllEvents(Pageable pageable);

    EventResponse updateEvent(Long id, EventRequest eventRequest);

    EventResponse deleteEvent(Long id);

    void generateTicket(TicketGenerationEvent data);
}
