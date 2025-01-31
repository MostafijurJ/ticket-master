package com.learn.ms.event.core.domain.request;

import com.learn.ms.event.core.domain.enums.TicketCategory;
import com.learn.ms.event.core.domain.model.DynamicId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class EventRequest implements Serializable {

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;

    @NotNull(message = "Event date is mandatory")
    private Date eventDate;

    @NotBlank(message = "Location is mandatory")
    private String location;

    @NotNull(message = "Venue is mandatory")
    private DynamicId venue;

    @NotNull(message = "Performers are mandatory")
    private List<DynamicId> performers;

    @NotNull(message = "Tickets are mandatory")
    private Map<TicketCategory, Integer> tickets;
}
