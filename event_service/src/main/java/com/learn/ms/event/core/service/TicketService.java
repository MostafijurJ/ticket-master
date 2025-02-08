package com.learn.ms.event.core.service;

import com.learn.ms.event.common.mapper.TicketMapper;
import com.learn.ms.event.core.domain.enums.TicketCategory;
import com.learn.ms.event.core.domain.enums.TicketStatus;
import com.learn.ms.event.core.domain.response.TicketResponse;
import com.learn.ms.event.data.entity.Ticket;
import com.learn.ms.event.data.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TicketService extends BaseService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    private static final String EVENT_TICKETS_AVAILABLE = "event:%s:tickets:available:%s";
    private static final String EVENT_TICKETS_BOOKED = "event:%d:tickets:booked";
    private static final String EVENT_TICKETS_KEY = "event:%d:tickets";

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

    // need a method which will return list of available tickets for a given event and category
    public List<TicketResponse> getAvailableTickets(Long eventId) {
        String availableTicketsKey = String.format(EVENT_TICKETS_AVAILABLE, eventId, TicketCategory.REGULAR.name());
        Set<Object> members = redisTemplate.opsForSet().members(availableTicketsKey);

        return List.of();

    }


    public Map<String, Object> getAvailableTicketCount(Long eventId) {
        String availableTicketsKey = String.format(EVENT_TICKETS_AVAILABLE, eventId.toString(), TicketCategory.REGULAR.name());
        Long availableRegular = redisTemplate.opsForSet().size(availableTicketsKey);


        redisTemplate.opsForSet().add("devtest", "Your need to get the value from the database");

        String availablePremiumKey = String.format(EVENT_TICKETS_AVAILABLE, eventId, TicketCategory.PREMIUM.name());
        Long premium = redisTemplate.opsForSet().size(availablePremiumKey);

        Map<String, Object> response = new HashMap<>();
        response.put(TicketCategory.REGULAR.name(), availableRegular);
        response.put(TicketCategory.PREMIUM.name(), premium);

        return response;
    }


    private void updateTicketStatusInRedis(String ticketId, String bookedTicketsKey) {
        redisTemplate.opsForHash().put(ticketId, "status", TicketStatus.BOOKED.name());
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
        ticketDetails.put("status", ticket.getStatus().name());
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
