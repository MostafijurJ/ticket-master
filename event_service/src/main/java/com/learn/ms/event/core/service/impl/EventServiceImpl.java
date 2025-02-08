package com.learn.ms.event.core.service.impl;

import com.learn.ms.event.common.mapper.EventMapper;
import com.learn.ms.event.common.mapper.PerformerMapper;
import com.learn.ms.event.common.mapper.VenueMapper;
import com.learn.ms.event.common.utils.DateTimeUtils;
import com.learn.ms.event.core.domain.enums.EventStatus;
import com.learn.ms.event.core.domain.enums.FeatureCode;
import com.learn.ms.event.core.domain.enums.NotificationType;
import com.learn.ms.event.core.domain.enums.ResponseMessage;
import com.learn.ms.event.core.domain.enums.TicketCategory;
import com.learn.ms.event.core.domain.enums.TicketStatus;
import com.learn.ms.event.core.domain.event.EventDataForElastic;
import com.learn.ms.event.core.domain.event.NotificationTemplateEvent;
import com.learn.ms.event.core.domain.event.TicketGenerationEvent;
import com.learn.ms.event.core.domain.exceptions.RecordNotFoundException;
import com.learn.ms.event.core.domain.model.DynamicId;
import com.learn.ms.event.core.domain.request.EventRequest;
import com.learn.ms.event.core.domain.response.EventResponse;
import com.learn.ms.event.core.domain.response.PerformerResponse;
import com.learn.ms.event.core.domain.response.VenueResponse;
import com.learn.ms.event.core.service.BaseService;
import com.learn.ms.event.core.service.EventService;
import com.learn.ms.event.data.entity.Event;
import com.learn.ms.event.data.entity.Performer;
import com.learn.ms.event.data.entity.Ticket;
import com.learn.ms.event.data.entity.Venue;
import com.learn.ms.event.data.repository.EventRepository;
import com.learn.ms.event.data.repository.PerformerRepository;
import com.learn.ms.event.data.repository.TicketRepository;
import com.learn.ms.event.data.repository.VenueRepository;
import com.learn.ms.event.presenter.producer.ProducerService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final RedisTemplate<String, String> redisTemplate;
    private final ProducerService producerService;

    private static final String EVENT_TICKETS_AVAILABLE = "event:%s:tickets:available:%s";



    @Override
    @Transactional
    public EventResponse createEvent(EventRequest eventRequest) {
        Event event = getEventEntity(eventRequest);

        // check if the performers are exists or not and set it to the event
        List<Performer> performers = addPerformers(eventRequest.getPerformers(), event);

        // check if the venue is existing or not and set it to the event
        Venue venue = addVenue(eventRequest.getVenue(), event);

        // Save the Event entity
        Event savedEvent = eventRepository.save(event);

        // Generate tickets for the event

        TicketGenerationEvent ticketGenerationEvent = new TicketGenerationEvent()
                .setEventId(savedEvent.getId())
                .setEventName(savedEvent.getName())
                .setTickets(eventRequest.getTickets());

        producerService.produceTicketGenerationEvent(ticketGenerationEvent, getCorrelationId());

        EventResponse eventResponse = createEventResponse(savedEvent, venue, performers);

        // Push the event data to ElasticSearch
        producerService.produceElasticSearchDate(getEventDataForElastic(eventResponse), getCorrelationId());

        // sent notification to the users

        producerService.produceNotificationEvent(getNotificationTemplateEvent(eventResponse), getCorrelationId());

        return eventResponse;
    }


    private NotificationTemplateEvent getNotificationTemplateEvent(EventResponse eventResponse) {
        NotificationTemplateEvent event = new NotificationTemplateEvent();

        event.setNotificationTypes(List.of(NotificationType.EMAIL));
        event.setToEmailList(List.of("ticketmaster721@yopmail.com"));
        event.setFeatureCode(FeatureCode.EVENT_CREATION.getCode());

        Map<String, Object> additionalFields = new HashMap<>();

        additionalFields.put("emailSubject", "Event Created Successfully");
        additionalFields.put("name", "Mostafijur Rahman");
        additionalFields.put("eventName", eventResponse.getName());
        additionalFields.put("eventDate", DateTimeUtils.formatDate(eventResponse.getEventDate()));
        additionalFields.put("eventStatus", eventResponse.getStatus());
        additionalFields.put("venueName", eventResponse.getVenueResponse().getName());
        additionalFields.put("venueAddress", eventResponse.getVenueResponse().getAddress());
        additionalFields.put("venueLocation", eventResponse.getVenueResponse().getLocation());

        additionalFields.put("performers", eventResponse.getPerformerResponses());

        event.setAdditionalFields(additionalFields);


        return event;
    }

    private EventDataForElastic getEventDataForElastic(EventResponse eventResponse) {
        return new EventDataForElastic()
                .setEventResponse(eventResponse)
                .setDetailsUrl("/api/v1/events/get/{id}");
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

        EventResponse eventResponse = createEventResponse(event, venue, performers);
        producerService.produceElasticSearchDate(getEventDataForElastic(eventResponse), getCorrelationId());
        return eventResponse;
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


    @Override
    public void generateTicket(TicketGenerationEvent eventRequest) {
        List<Ticket> tickets = new ArrayList<>();
        eventRequest.getTickets().forEach((category, ticketPrice) -> {
            for (int i = 1; i <= ticketPrice.getNumberOfTickets(); i++) {
                Ticket ticket = new Ticket();
                ticket.setName(generateTicketName(eventRequest.getEventName(), category, i));
                ticket.setPrice(BigDecimal.valueOf(ticketPrice.getPrice()));
                ticket.setSeatNumber(generateSeatNumber(category.name(), i));
                ticket.setCategory(category);
                ticket.setStatus(TicketStatus.AVAILABLE);
                ticket.setBooked(false);
                ticket.setEventId(eventRequest.getEventId());
                tickets.add(ticket);
            }
        });

        // Save the tickets
        ticketRepository.saveAll(tickets);

        // PUSH THIS TICKETS TO REDIS
        pushTicketsToRedis(tickets, eventRequest);
    }

    private String generateTicketName(String eventName, TicketCategory category, int i) {
        return eventName + " - " + category + " - Ticket " + i;
    }

    private String generateSeatNumber(String category, int i) {
        return category + "-" + i;
    }

    private void pushTicketsToRedis(List<Ticket> tickets, TicketGenerationEvent event) {
        for (Ticket ticket : tickets) {
            String ticketId = "ticket:" + ticket.getId();
            String eventTicketsKey = String.format(EVENT_TICKETS_AVAILABLE, event.getEventId().toString(), ticket.getCategory().name());

            // Store ticket details in Redis Hash
            Map<String, String> ticketDetails = new HashMap<>();
            ticketDetails.put("id", ticket.getId().toString());
            ticketDetails.put("category", ticket.getCategory().name());
            ticketDetails.put("status", ticket.getStatus().name());
            ticketDetails.put("eventId", event.getEventId().toString());
            ticketDetails.put("seatNumber", ticket.getSeatNumber());

            redisTemplate.opsForHash().putAll(ticketId, ticketDetails);
            // Add ticket ID to the available tickets set for the category
            Long add = redisTemplate.opsForSet().add(eventTicketsKey, ticketId);
            System.out.println("Added to set [" + eventTicketsKey + "]: " + ticketId + " (Success: " + add + ")");

        }
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
                .setVenueResponse(venueResponse)
                .setPerformerResponses(performerResponseList);
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
