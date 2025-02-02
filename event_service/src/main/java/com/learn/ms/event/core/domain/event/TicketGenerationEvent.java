package com.learn.ms.event.core.domain.event;

import com.learn.ms.event.core.domain.enums.TicketCategory;
import com.learn.ms.event.core.domain.model.TicketPrice;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class TicketGenerationEvent {
    private Long eventId;
    private String eventName;
    private Map<TicketCategory, TicketPrice> tickets;

}
