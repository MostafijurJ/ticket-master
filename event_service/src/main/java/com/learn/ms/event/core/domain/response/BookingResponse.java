package com.learn.ms.event.core.domain.response;

import com.learn.ms.event.core.domain.enums.TicketCategory;
import com.learn.ms.event.core.domain.enums.TicketStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BookingResponse implements Serializable {
    private String name;
    private String email;
    private String phone;
    private String pnrNumber;
    private BigDecimal price;
    private String seatNumber;
    private TicketCategory category;
    private TicketStatus status;
}
