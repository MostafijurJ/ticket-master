package com.learn.ms.search.core.domain.response;

import com.learn.ms.search.core.domain.enums.EventStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class EventElasticResponse {
    private String detailsUrl;
    private Long eventId;
    private String eventName;
    private String description;
    private Date eventDate;
    private EventStatus eventStatus;

    private VenueResponse venueResponse;
    private List<PerformerResponse> performerResponses;
}
