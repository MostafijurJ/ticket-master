package com.learn.ms.event.core.service;

import com.learn.ms.event.common.mapper.EventMapper;
import com.learn.ms.event.core.domain.enums.EventStatus;
import com.learn.ms.event.core.domain.enums.ResponseMessage;
import com.learn.ms.event.core.domain.exceptions.RecordNotFoundException;
import com.learn.ms.event.core.domain.model.PerformerDTO;
import com.learn.ms.event.core.domain.model.VenueDTO;
import com.learn.ms.event.core.domain.request.EventRequest;
import com.learn.ms.event.core.domain.response.EventResponse;
import com.learn.ms.event.data.entity.Event;
import com.learn.ms.event.data.entity.Performer;
import com.learn.ms.event.data.repository.EventRepository;
import com.learn.ms.event.data.repository.PerformerRepository;
import com.learn.ms.event.data.repository.TicketRepository;
import com.learn.ms.event.data.repository.VenueRepository;
import com.learn.ms.event.presenter.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl extends BaseService implements EventService {
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final PerformerRepository performerRepository;
    private final VenueRepository venueRepository;
    private final TicketRepository ticketRepository;


    @Override
    public EventResponse createEvent(EventRequest eventRequest) {
        Event event = new Event();
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setEventDate(eventRequest.getEventDate());
        event.setStatus(EventStatus.CREATED);

        // check if the performers are exists or not and set it to the event
        eventRequest.getPerformers().forEach(performerId -> {
            Performer performer = performerRepository.findById(performerId.getId())
                    .orElseThrow(
                            () -> new RecordNotFoundException("Performer not found with id: " + performerId.getId()));
            event.getPerformers().add(performer);
        });

        // check if the venue is exists or not and set it to the event
        event.setVenue(venueRepository.findById(eventRequest.getVenue().getId())
                .orElseThrow(() -> new RecordNotFoundException("Venue not found with id: " + eventRequest.getVenue().getId())));

        // create tickets for the event category and set it to the event

        // ticket generation logic will be implemented here


        // Save the Event entity
        Event savedEvent = eventRepository.save(event);


        return eventMapper.mapToResponse(savedEvent);
    }

    @Override
    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND));
        return eventMapper.mapToResponse(event);
    }

    @Override
    public Page<EventResponse> getAllEvents(Pageable pageable) {
        return eventRepository.findAllByOrderByIdDesc(pageable)
                .map(eventMapper::mapToResponse);
    }

    @Override
    public EventResponse updateEvent(Long id, EventRequest eventRequest) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND));

        updateEventDetails(event, eventRequest);
        updateEventPerformers(event, eventRequest.getPerformers());
        updateEventVenue(event, eventRequest.getVenue());


        return eventMapper.mapToResponse(eventRepository.save(event));
    }

    private void updateEventDetails(Event event, EventRequest eventRequest) {
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setEventDate(eventRequest.getEventDate());
    }

    private void updateEventPerformers(Event event, List<PerformerDTO> performerDTOs) {
        if (CollectionUtils.isEmpty(performerDTOs)) {
            return;
        }

        event.getPerformers().clear();
        for (PerformerDTO performerDTO : performerDTOs) {
            Performer performer = performerRepository.findById(performerDTO.getId())
                    .orElseThrow(() -> new RecordNotFoundException("Performer not found with id: " + performerDTO.getId()));
            event.getPerformers().add(performer);
        }

    }

    private void updateEventVenue(Event event, VenueDTO venueDTO) {
        if (ObjectUtils.isEmpty(venueDTO))
            return;
        event.setVenue(venueRepository.findById(venueDTO.getId())
                .orElseThrow(() -> new RecordNotFoundException("Venue not found with id: " + venueDTO.getId())));
    }

    @Override
    public EventResponse deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(ResponseMessage.RECORD_NOT_FOUND));
        event.setActive(false);
        event.setStatus(EventStatus.CANCELLED);
        eventRepository.save(event);
        return eventMapper.mapToResponse(event);
    }
}
