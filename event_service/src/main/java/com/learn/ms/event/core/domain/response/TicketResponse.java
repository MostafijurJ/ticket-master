package com.learn.ms.event.core.domain.response;

import com.learn.ms.event.core.domain.enums.TicketCategory;
import com.learn.ms.event.core.domain.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse implements Serializable {
    private Long id;
    private String seatNumber;
    private TicketCategory category;
    private TicketStatus status;
    private Boolean booked;
    private Long eventId;
}
