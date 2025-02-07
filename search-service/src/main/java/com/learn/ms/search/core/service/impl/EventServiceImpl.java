package com.learn.ms.search.core.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.learn.ms.search.core.domain.event.EventDataForElastic;
import com.learn.ms.search.core.domain.exceptions.OperationFailedException;
import com.learn.ms.search.core.domain.response.EventElasticResponse;
import com.learn.ms.search.core.domain.response.EventResponse;
import com.learn.ms.search.core.domain.response.PerformerResponse;
import com.learn.ms.search.core.domain.response.VenueResponse;
import com.learn.ms.search.core.service.BaseService;
import com.learn.ms.search.core.service.IEventService;
import com.learn.ms.search.data.entity.EventElastic;
import com.learn.ms.search.data.repository.EventElasticRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl extends BaseService implements IEventService {
    private final EventElasticRepository eventElasticRepository;
    private final ElasticsearchClient elasticsearchClient;


    @Override
    public List<EventElasticResponse> searchEvents(String query) {
        logger.trace("============= Searching events in elastic search ==============");
        printTrace(query);
        try {
            SearchResponse<EventElastic> response = elasticsearchClient.search(s -> s
                            .index("events")
                            .query(q -> q
                                    .multiMatch(m -> m
                                            .fields("eventName", "venueResponse.name", "venueResponse.location",
                                                    "eventDate", "performerResponses.name")
                                            .query(query)
                                    )
                            )
                            .collapse(c -> c.field("eventId")), // Collapse to avoid duplicates
                    EventElastic.class
            );

            logger.trace("============= Events found in elastic search ==============");
            printTrace(response);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .map(this::mapToEventElasticResponse)
                    .toList();
        } catch (IOException e) {
            logger.error("Failed to search events in elastic search", e);
            throw new OperationFailedException("Failed to search events in elastic search");
        }
    }


    private EventElasticResponse mapToEventElasticResponse(EventElastic eventElastic) {
        return new EventElasticResponse()
                .setDetailsUrl(eventElastic.getDetailsUrl())
                .setEventId(eventElastic.getEventId())
                .setEventName(eventElastic.getEventName())
                .setDescription(eventElastic.getDescription())
                .setEventDate(eventElastic.getEventDate())
                .setEventStatus(eventElastic.getEventStatus())
                .setVenueResponse(eventElastic.getVenueResponse())
                .setPerformerResponses(eventElastic.getPerformerResponses());
    }

    @Override
    public void pushDataToElastic(EventDataForElastic eventDataForElastic) {
        logger.trace("============= Pushing data to elastic search ==============");
        printTrace(eventDataForElastic);
        EventElastic eventElastic = new EventElastic();
        eventElastic.setDetailsUrl(eventDataForElastic.getDetailsUrl());

        // set event data
        if (ObjectUtils.isNotEmpty(eventDataForElastic.getEventResponse())) {
            setEventData(eventDataForElastic.getEventResponse(), eventElastic);
        }

        if (ObjectUtils.isNotEmpty(eventDataForElastic.getEventResponse().getVenueResponse())) {
            setVenueData(eventDataForElastic.getEventResponse().getVenueResponse(), eventElastic);
        }

        if (ObjectUtils.isNotEmpty(eventDataForElastic.getEventResponse().getPerformerResponses())) {
            setPerformersData(eventDataForElastic.getEventResponse().getPerformerResponses(), eventElastic);
        }

        eventElasticRepository.save(eventElastic);

        logger.trace("============= Data pushed to elastic search ==============");
    }

    private void setPerformersData(List<PerformerResponse> performerResponses, EventElastic eventElastic) {
        eventElastic.setPerformerResponses(performerResponses);
    }

    private void setVenueData(VenueResponse venueResponse, EventElastic eventElastic) {
        eventElastic.setVenueResponse(venueResponse);
    }

    private void setEventData(EventResponse eventResponse, EventElastic eventElastic) {
        eventElastic.setEventId(eventResponse.getId());
        eventElastic.setEventName(eventResponse.getName());
        eventElastic.setDescription(eventResponse.getDescription());
        eventElastic.setEventStatus(eventResponse.getStatus());
    }
}
