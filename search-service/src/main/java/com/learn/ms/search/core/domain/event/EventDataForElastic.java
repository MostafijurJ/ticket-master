package com.learn.ms.search.core.domain.event;

import com.learn.ms.search.core.domain.response.EventResponse;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EventDataForElastic {
    private EventResponse eventResponse;
    private String detailsUrl;
}
