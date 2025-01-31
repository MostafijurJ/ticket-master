package com.learn.ms.event.core.service;

import com.learn.ms.event.common.mapper.EventMapper;
import com.learn.ms.event.common.mapper.PerformerMapper;
import com.learn.ms.event.common.mapper.VenueMapper;
import com.learn.ms.event.core.domain.enums.EventStatus;
import com.learn.ms.event.core.domain.enums.ResponseMessage;
import com.learn.ms.event.core.domain.exceptions.RecordNotFoundException;
import com.learn.ms.event.core.domain.model.DynamicId;
import com.learn.ms.event.core.domain.request.EventRequest;
import com.learn.ms.event.core.domain.response.EventResponse;
import com.learn.ms.event.core.domain.response.PerformerResponse;
import com.learn.ms.event.core.domain.response.VenueResponse;
import com.learn.ms.event.data.entity.Event;
import com.learn.ms.event.data.entity.Performer;
import com.learn.ms.event.data.entity.Venue;
import com.learn.ms.event.data.repository.EventRepository;
import com.learn.ms.event.data.repository.PerformerRepository;
import com.learn.ms.event.data.repository.TicketRepository;
import com.learn.ms.event.data.repository.VenueRepository;
import com.learn.ms.event.presenter.service.EventService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl extends BaseService implements EventService {
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final PerformerRepository performerRepository;
    private final VenueRepository venueRepository;
    private final TicketRepository ticketRepository;
    private final VenueMapper venueMapper;
    private final PerformerMapper performerMapper;


    @Override
    @Transactional
    public EventResponse createEvent(EventRequest eventRequest) {
        Event event = getEventEntity(eventRequest);

        // check if the performers are exists or not and set it to the event
        List<Performer> performers = addPerformers(eventRequest.getPerformers(), event);

        // check if the venue is existing or not and set it to the event
        Venue venue = addVenue(eventRequest.getVenue(), event);

        // create tickets for the event category and set it to the event
        // ticket generation logic will be implemented here

        // Save the Event entity
        Event savedEvent = eventRepository.save(event);
        return createEventResponse(savedEvent, venue, performers);
    }

    @Override
    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND));
        Venue venue = toObject(event.getVenue(), Venue.class);
        List<Performer> performers = toObjectList(event.getPerformers(), Performer.class);
        return createEventResponse(event, venue, performers);
    }

    @Override
    public Page<EventResponse> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable)
                .map(event -> {
                    Venue venue = toObject(event.getVenue(), Venue.class);
                    List<Performer> performers = toObjectList(event.getPerformers(), Performer.class);
                    return createEventResponse(event, venue, performers);
                });

    }

    @Override
    @Transactional
    public EventResponse updateEvent(Long id, EventRequest eventRequest) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND));

        updateEventDetails(event, eventRequest);
        List<Performer> performers = addPerformers(eventRequest.getPerformers(), event);

        Venue venue = null;
        if (!ObjectUtils.isEmpty(eventRequest.getVenue())) {
            venue = addVenue(eventRequest.getVenue(), event);
        }

        eventRepository.save(event);
        return createEventResponse(event, venue, performers);
    }


    @Override
    public EventResponse deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND));
        event.setActive(false);
        event.setStatus(EventStatus.CANCELLED);
        eventRepository.save(event);

         Venue venue = toObject(event.getVenue(), Venue.class);
         List<Performer> performers = toObjectList(event.getPerformers(), Performer.class);
        return createEventResponse(event, venue, performers);
    }


    private void updateEventDetails(Event event, EventRequest eventRequest) {
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setEventDate(eventRequest.getEventDate());
    }

    private EventResponse createEventResponse(Event event, Venue venue, List<Performer> performers) {
        VenueResponse venueResponse = venueMapper.mapToResponse(venue);
        List<PerformerResponse> performerResponseList = performers.stream()
                .map(performerMapper::mapToResponse)
                .toList();

        return eventMapper.mapToResponse(event)
                .setVenue(venueResponse)
                .setPerformers(performerResponseList);
    }

    private Venue addVenue(@NotNull(message = "Venue is mandatory") DynamicId venueId, Event event) {
        Venue venue = venueRepository.findById(venueId.getId())
                .orElseThrow(() -> new RecordNotFoundException("Venue not found with id: " + venueId.getId()));
        event.setVenue(writeJsonString(venue));

        return venue;
    }

    private List<Performer> addPerformers(List<DynamicId> performerIdList, Event event) {
        if (CollectionUtils.isEmpty(performerIdList)) {
            return List.of();
        }
        List<Performer> performers = new ArrayList<>();
        for (DynamicId id : performerIdList) {
            Performer performer = performerRepository.findById(id.getId())
                    .orElseThrow(() -> new RecordNotFoundException("Performer not found with id: " + id.getId()));
            performers.add(performer);
        }

        event.setPerformers(writeJsonString(performers));

        return performers;
    }

    private Event getEventEntity(EventRequest eventRequest) {
        Event event = new Event();
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setEventDate(eventRequest.getEventDate());
        event.setStatus(EventStatus.CREATED);
        return event;
    }


}
