package com.learn.ms.event.core.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TicketPrice implements Serializable {
    private Integer numberOfTickets;
    private Double price;
}
