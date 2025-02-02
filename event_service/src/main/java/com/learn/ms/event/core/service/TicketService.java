package com.learn.ms.event.core.service;

import com.learn.ms.event.core.domain.enums.TicketCategory;
import com.learn.ms.event.core.domain.enums.TicketStatus;
import com.learn.ms.event.data.entity.Ticket;
import com.learn.ms.event.data.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TicketService extends BaseService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final TicketRepository ticketRepository;

    private static final String EVENT_TICKETS_AVAILABLE = "event:%d:tickets:available:%s";
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

    public long getAvailableTicketCount(Long eventId, TicketCategory category) {
        String availableTicketsKey = String.format(EVENT_TICKETS_AVAILABLE, eventId, category.name());
        Long size = redisTemplate.opsForSet().size(availableTicketsKey);
        return size != null ? size : 0;
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
