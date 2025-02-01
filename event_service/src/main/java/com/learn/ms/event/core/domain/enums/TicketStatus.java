package com.learn.ms.event.core.domain.enums;

import lombok.Getter;

@Getter
public enum TicketStatus {
    AVAILABLE,
    HOLD,
    BOOKED,
    CANCELLED
}
