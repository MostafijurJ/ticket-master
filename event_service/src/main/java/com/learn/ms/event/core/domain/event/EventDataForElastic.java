package com.learn.ms.event.core.domain.event;

import com.learn.ms.event.core.domain.response.EventResponse;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EventDataForElastic {
    private EventResponse eventResponse;
    private String detailsUrl;
}
