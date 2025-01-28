package com.learn.ms.event.common.mapper;


import com.learn.ms.event.core.domain.response.EventResponse;
import com.learn.ms.event.data.entity.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventResponse mapToResponse(final Event entity);

}
