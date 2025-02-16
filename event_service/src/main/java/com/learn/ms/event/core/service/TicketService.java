package com.learn.ms.event.core.service;

import com.learn.ms.event.common.mapper.TicketMapper;
import com.learn.ms.event.core.domain.enums.TicketCategory;
import com.learn.ms.event.core.domain.enums.TicketStatus;
import com.learn.ms.event.core.domain.response.TicketResponse;
import com.learn.ms.event.data.entity.Ticket;
import com.learn.ms.event.data.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TicketService extends BaseService {
    public static final String STATUS = "status";
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;


    @Transactional
    public boolean bookTicket(Long eventId, TicketCategory category) {
        String availableTicketsKey = String.format(EVENT_TICKETS_AVAILABLE, eventId, category.name());
        String bookedTicketsKey = String.format(EVENT_TICKETS_BOOKED, eventId);

        String ticketId = (String) redisTemplate.opsForSet().pop(availableTicketsKey);

        if (ticketId == null) {
            return false; // No tickets available
        }

        updateTicketStatusInRedis(ticketId, bookedTicketsKey);
        updateTicketStatusInDatabase(ticketId);

        return true; // Ticket booked successfully
    }

    @Transactional
    public void synchronizeRedisWithDatabase(Long eventId) {
        List<Ticket> tickets = ticketRepository.findAllByEventIdAndActiveTrue(eventId);

        for (Ticket ticket : tickets) {
            String ticketId = "ticket:" + ticket.getId();
            String eventTicketsKey = String.format(EVENT_TICKETS_KEY, eventId);

            updateRedisWithTicketDetails(ticket, ticketId);
            addTicketIdToRedisSet(ticket, eventTicketsKey, eventId);
        }
    }

    public List<TicketResponse> getAvailableTickets(Long eventId) {
        List<TicketResponse> availableTickets = new ArrayList<>();
        availableTickets.addAll(getTicketsByCategory(eventId, TicketCategory.REGULAR));
        availableTickets.addAll(getTicketsByCategory(eventId, TicketCategory.PREMIUM));
        return availableTickets;
    }

    private List<TicketResponse> getTicketsByCategory(Long eventId, TicketCategory category) {
        List<TicketResponse> tickets = new ArrayList<>();
        String ticketsKey = String.format(EVENT_TICKETS_AVAILABLE, eventId.toString(), category.name());
        Set<Object> ticketIds = redisTemplate.opsForSet().members(ticketsKey);

        if (CollectionUtils.isEmpty(ticketIds)) {
            logger.trace("No tickets available for event: " + eventId + " and category: " + category);
            return tickets;
        }

        for (Object ticketId : ticketIds) {
            Map<Object, Object> ticketDetails = redisTemplate.opsForHash().entries(ticketId.toString());
            TicketResponse ticketResponse = mapToResponse(ticketDetails);
            tickets.add(ticketResponse);
        }
        return tickets;
    }


    private TicketResponse mapToResponse(Map<Object, Object> ticketDetails) {
        TicketResponse ticketResponse = new TicketResponse();
        ticketResponse.setId(Long.parseLong((String) ticketDetails.get("id")));
        ticketResponse.setCategory(TicketCategory.valueOf((String) ticketDetails.get("category")));
        ticketResponse.setStatus(TicketStatus.valueOf((String) ticketDetails.get(STATUS)));
        ticketResponse.setEventId(Long.parseLong((String) ticketDetails.get("eventId")));
        ticketResponse.setSeatNumber((String) ticketDetails.get("seatNumber"));

        ticketResponse.setBooked(TicketStatus.BOOKED.equals(ticketResponse.getStatus()));

        return ticketResponse;
    }


    public Map<String, Object> getAvailableTicketCount(Long eventId) {
        String availableTicketsKey = String.format(EVENT_TICKETS_AVAILABLE, eventId.toString(), TicketCategory.REGULAR.name());
        Long availableRegular = redisTemplate.opsForSet().size(availableTicketsKey);


        String availablePremiumKey = String.format(EVENT_TICKETS_AVAILABLE, eventId.toString(), TicketCategory.PREMIUM.name());
        Long premium = redisTemplate.opsForSet().size(availablePremiumKey);

        Map<String, Object> response = new HashMap<>();
        response.put(TicketCategory.REGULAR.name(), availableRegular);
        response.put(TicketCategory.PREMIUM.name(), premium);

        return response;
    }


    private void updateTicketStatusInRedis(String ticketId, String bookedTicketsKey) {
        redisTemplate.opsForHash().put(ticketId, STATUS, TicketStatus.BOOKED.name());
        redisTemplate.opsForSet().add(bookedTicketsKey, ticketId);
    }

    private void updateTicketStatusInDatabase(String ticketId) {
        Long ticketDbId = Long.parseLong(ticketId.split(":")[1]);
        Ticket ticket = ticketRepository.findById(ticketDbId).orElseThrow();
        ticket.setBooked(true);
        ticket.setStatus(TicketStatus.BOOKED);
        ticketRepository.save(ticket);
    }


    private void updateRedisWithTicketDetails(Ticket ticket, String ticketId) {
        Map<String, String> ticketDetails = new HashMap<>();
        ticketDetails.put("id", ticket.getId().toString());
        ticketDetails.put("category", ticket.getCategory().name());
        ticketDetails.put(STATUS, ticket.getStatus().name());
        ticketDetails.put("eventId", ticket.getEventId().toString());
        ticketDetails.put("seatNumber", ticket.getSeatNumber());

        redisTemplate.opsForHash().putAll(ticketId, ticketDetails);
    }

    private void addTicketIdToRedisSet(Ticket ticket, String eventTicketsKey, Long eventId) {
        if (TicketStatus.AVAILABLE.equals(ticket.getStatus())) {
            redisTemplate.opsForSet().add(eventTicketsKey, ticket.getId());
        } else if (TicketStatus.BOOKED.equals(ticket.getStatus())) {
            String bookedTicketsKey = String.format(EVENT_TICKETS_BOOKED, eventId);
            redisTemplate.opsForSet().add(bookedTicketsKey, ticket.getId());
        }
    }
}
