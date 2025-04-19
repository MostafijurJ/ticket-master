package com.learn.ms.event.core.service;

import com.learn.ms.event.core.domain.enums.EventStatus;
import com.learn.ms.event.data.entity.Event;
import com.learn.ms.event.data.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventStatusUpdater extends BaseService {
    private final EventRepository eventRepository;

    @Scheduled(fixedRate = 300000) // Runs every 5 minutes
    public void updateEventStatus() {
        List<Event> events = eventRepository.findByEventDateBeforeAndStatus(getCurrentDate(), EventStatus.CREATED);
        logger.trace("Events to be updated: {}", events.size());
        for (Event event : events) {
            event.setStatus(EventStatus.FINISHED);
            eventRepository.save(event);
        }
    }
}
