package com.learn.ms.search.core.domain.response;

import com.learn.ms.search.core.domain.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Date eventDate;
    private EventStatus status;
    private VenueResponse venueResponse;
    private List<PerformerResponse> performerResponses;
}
