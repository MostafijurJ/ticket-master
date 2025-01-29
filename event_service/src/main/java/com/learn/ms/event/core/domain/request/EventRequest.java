package com.learn.ms.event.core.domain.request;

import com.learn.ms.event.core.domain.enums.TicketCategory;
import com.learn.ms.event.core.domain.model.PerformerDTO;
import com.learn.ms.event.core.domain.model.VenueDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class EventRequest implements Serializable {
    private String name;
    private String description;

    private Date eventDate;
    private String location;
    private VenueDTO venue;
    private List<PerformerDTO> performers;
    private Map<TicketCategory, Integer> tickets;
}
