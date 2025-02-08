package com.learn.ms.event.common.mapper;

import com.learn.ms.event.core.domain.response.TicketResponse;
import com.learn.ms.event.data.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketResponse mapToResponse(final Ticket entity);

}
