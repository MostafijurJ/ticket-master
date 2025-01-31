package com.learn.ms.event.common.mapper;

import com.learn.ms.event.core.domain.request.PerformerRequest;
import com.learn.ms.event.core.domain.response.PerformerResponse;
import com.learn.ms.event.data.entity.Performer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PerformerMapper {

    PerformerResponse mapToResponse(final Performer entity);

    Performer mapRequestToEntity(final PerformerRequest request);

}
